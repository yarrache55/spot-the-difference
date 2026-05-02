/**
 * game.js — Spot the Difference core game logic
 *
 * Reads config from #gameData data attributes injected by Thymeleaf.
 * Sends AJAX clicks to /game/click, processes JSON response.
 */

(function () {
    'use strict';

    // ── READ CONFIG ──────────────────────────────────────────────
    const gameDataEl    = document.getElementById('gameData');
    const LEVEL         = parseInt(gameDataEl.dataset.level);
    const TIME_LIMIT    = parseInt(gameDataEl.dataset.timeLimit);   // 0 = no limit
    const TOTAL_DIFFS   = parseInt(gameDataEl.dataset.totalDiffs);
    const CSRF_TOKEN    = gameDataEl.dataset.csrfToken;
    const CSRF_HEADER   = gameDataEl.dataset.csrfHeader;

    // ── STATE ────────────────────────────────────────────────────
    let foundCount      = parseInt(gameDataEl.dataset.found) || 0;
    let score           = 0;
    let timeLeft        = TIME_LIMIT;
    let timerInterval   = null;
    let gameOver        = false;

    // ── DOM REFS ─────────────────────────────────────────────────
    const modifiedImg       = document.getElementById('modifiedImg');
    const modifiedContainer = document.getElementById('modifiedContainer');
    const originalContainer = document.getElementById('originalContainer');
    const scoreDisplay      = document.getElementById('scoreDisplay');
    const foundCountEl      = document.getElementById('foundCount');
    const timerDisplay      = document.getElementById('timerDisplay');
    const progressBar       = document.getElementById('progressBar');
    const clickFeedback     = document.getElementById('clickFeedback');
    const levelCompleteModal = document.getElementById('levelCompleteModal');
    const timeUpModal       = document.getElementById('timeUpModal');
    const modalScore        = document.getElementById('modalScore');

    // ── TIMER ────────────────────────────────────────────────────
    function startTimer() {
        if (TIME_LIMIT <= 0) {
            if (timerDisplay) timerDisplay.textContent = '∞';
            return;
        }
        updateTimerDisplay();
        timerInterval = setInterval(() => {
            timeLeft--;
            updateTimerDisplay();
            if (timeLeft <= 0) {
                clearInterval(timerInterval);
                if (!gameOver) {
                    gameOver = true;
                    showModal(timeUpModal);
                }
            }
        }, 1000);
    }

    function updateTimerDisplay() {
        if (!timerDisplay) return;
        const mins = Math.floor(timeLeft / 60);
        const secs = timeLeft % 60;
        timerDisplay.textContent = mins > 0
            ? `${mins}:${secs.toString().padStart(2, '0')}`
            : `${timeLeft}s`;

        if (timeLeft <= 15 && TIME_LIMIT > 0) {
            timerDisplay.classList.add('timer-warning');
        } else {
            timerDisplay.classList.remove('timer-warning');
        }
    }

    // ── CLICK HANDLER ────────────────────────────────────────────
    modifiedImg.addEventListener('click', function (e) {
        if (gameOver) return;

        const rect = modifiedImg.getBoundingClientRect();
        const xPx = e.clientX - rect.left;
        const yPx = e.clientY - rect.top;
        const xPercent = (xPx / rect.width)  * 100;
        const yPercent = (yPx / rect.height) * 100;

        fetch('/game/click', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [CSRF_HEADER]: CSRF_TOKEN
            },
            body: JSON.stringify({ xPercent, yPercent, levelNumber: LEVEL })
        })
        .then(r => r.json())
        .then(data => handleClickResponse(data, xPercent, yPercent))
        .catch(err => console.error('Click error:', err));
    });

    // ── HINT HANDLER ────────────────────────────────────────────
    const hintButton = document.getElementById('hintButton');
    if (hintButton) {
        console.log('Hint button found');
        hintButton.addEventListener('click', function () {
            console.log('Hint button clicked');
            console.log('CSRF_HEADER:', CSRF_HEADER, 'CSRF_TOKEN:', CSRF_TOKEN);
            if (gameOver) return;

            fetch('/game/hint', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [CSRF_HEADER]: CSRF_TOKEN
                },
                body: JSON.stringify({ levelNumber: LEVEL })
            })
            .then(r => {
                console.log('Hint response status:', r.status);
                return r.json();
            })
            .then(data => {
                console.log('Hint data:', data);
                handleHintResponse(data);
            })
            .catch(err => console.error('Hint error:', err));
        });
    } else {
        console.error('Hint button not found');
    }

    // ── RESPONSE HANDLER ─────────────────────────────────────────
    function handleClickResponse(data, xPercent, yPercent) {
        if (data.error) { console.warn(data.error); return; }

        // Update HUD
        score = data.score || 0;
        if (scoreDisplay) scoreDisplay.textContent = score;
        if (foundCountEl) foundCountEl.textContent = data.differencesFound;

        // Update progress bar
        const pct = (data.differencesFound / TOTAL_DIFFS) * 100;
        if (progressBar) progressBar.style.width = pct + '%';

        if (data.hit && !data.alreadyFound) {
            // SUCCESS: new difference found
            placeMarker(data.xPercent, data.yPercent, 'found-marker', '✓');
            placeMarkerOnOriginal(data.xPercent, data.yPercent);
            showFeedback('TROUVÉ !!', 'show-found');
            pulseHud();
            foundCount++;

            if (data.levelComplete) {
                clearInterval(timerInterval);
                gameOver = true;
                if (modalScore) modalScore.textContent = score;
                // Show last level button or next level
                if (data.isLastLevel) {
                    document.getElementById('nextBtn').href = '/game/victory';
                    document.getElementById('nextBtn').textContent = '🏆 VICTOIRE !';
                }
                setTimeout(() => showModal(levelCompleteModal), 800);
            }

        } else if (data.hit && data.alreadyFound) {
            showFeedback('DÉJÀ TROUVÉ !', 'show-already');

        } else {
            // WRONG CLICK
            placeWrongClick(xPercent, yPercent);
            showFeedback('✗ RATÉ !', 'show-wrong');
            if (scoreDisplay) {
                scoreDisplay.classList.add('score-penalty');
                setTimeout(() => scoreDisplay.classList.remove('score-penalty'), 400);
            }
        }
    }

    // ── HINT RESPONSE HANDLER ─────────────────────────────────────
    function handleHintResponse(data) {
        console.log('Handling hint response:', data);
        if (data.error) {
            showFeedback(data.error, 'show-wrong');
            return;
        }
        // Place hint marker
        placeHintMarker(data.hintX, data.hintY);
    }

    // ── HINT MARKER PLACEMENT ─────────────────────────────────────
    function placeHintMarker(xPct, yPct) {
        const marker = document.createElement('div');
        marker.className = 'diff-marker hint-marker';
        marker.style.left = xPct + '%';
        marker.style.top = yPct + '%';
        marker.textContent = '?';
        marker.setAttribute('aria-hidden', 'true');
        modifiedContainer.appendChild(marker);
        // Remove after 3 seconds
        setTimeout(() => marker.remove(), 3000);
    }

    // ── MARKER PLACEMENT ─────────────────────────────────────────
    function placeMarker(xPct, yPct, className, text) {
        const marker = document.createElement('div');
        marker.className = `diff-marker ${className}`;
        marker.style.left = xPct + '%';
        marker.style.top  = yPct + '%';
        marker.textContent = text;
        marker.setAttribute('aria-hidden', 'true');
        modifiedContainer.appendChild(marker);
    }

    function placeMarkerOnOriginal(xPct, yPct) {
        if (!originalContainer) return;
        const marker = document.createElement('div');
        marker.className = 'diff-marker found-marker';
        marker.style.left = xPct + '%';
        marker.style.top  = yPct + '%';
        marker.textContent = '✓';
        marker.setAttribute('aria-hidden', 'true');
        originalContainer.appendChild(marker);
    }

    function placeWrongClick(xPct, yPct) {
        const marker = document.createElement('div');
        marker.className = 'diff-marker click-wrong';
        marker.style.left = xPct + '%';
        marker.style.top  = yPct + '%';
        marker.textContent = '✗';
        marker.setAttribute('aria-hidden', 'true');
        modifiedContainer.appendChild(marker);
        setTimeout(() => marker.remove(), 700);
    }

    // ── FEEDBACK BUBBLE ──────────────────────────────────────────
    function showFeedback(text, cssClass) {
        if (!clickFeedback) return;
        clickFeedback.className = 'click-feedback';
        clickFeedback.textContent = text;
        // Force reflow to restart animation
        void clickFeedback.offsetWidth;
        clickFeedback.classList.add(cssClass);
    }

    // ── HUD PULSE ────────────────────────────────────────────────
    function pulseHud() {
        if (!scoreDisplay) return;
        scoreDisplay.classList.add('hud-pulse');
        setTimeout(() => scoreDisplay.classList.remove('hud-pulse'), 400);
    }

    // ── MODAL ────────────────────────────────────────────────────
    function showModal(modal) {
        if (modal) modal.style.display = 'flex';
    }

    // Close modal on overlay click (only timeUp, not levelComplete)
    if (timeUpModal) {
        timeUpModal.addEventListener('click', (e) => {
            if (e.target === timeUpModal) timeUpModal.style.display = 'none';
        });
    }

    // ── ADD HUD PULSE STYLE ───────────────────────────────────────
    const style = document.createElement('style');
    style.textContent = `
        .hud-pulse { animation: hudPulse 0.4s ease; }
        @keyframes hudPulse {
            0%   { transform: scale(1);   color: var(--yellow); }
            50%  { transform: scale(1.4); color: #2ecc71; }
            100% { transform: scale(1);   color: var(--yellow); }
        }
        .score-penalty { animation: scorePenalty 0.4s ease; }
        @keyframes scorePenalty {
            0%,100% { color: var(--yellow); }
            50%     { color: var(--red-light); transform: scale(1.2); }
        }
    `;
    document.head.appendChild(style);

    // ── INIT ─────────────────────────────────────────────────────
    startTimer();

})();
