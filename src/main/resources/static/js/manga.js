/**
 * manga.js — Global UI helpers for the Manga Sketchbook theme
 */

// Ink-splash ripple on .manga-btn clicks
document.addEventListener('DOMContentLoaded', () => {

    // Ripple effect for buttons
    document.querySelectorAll('.manga-btn').forEach(btn => {
        btn.addEventListener('click', function (e) {
            const circle = document.createElement('span');
            const diameter = Math.max(this.clientWidth, this.clientHeight);
            const radius = diameter / 2;
            const rect = this.getBoundingClientRect();

            circle.style.cssText = `
                width: ${diameter}px;
                height: ${diameter}px;
                left: ${e.clientX - rect.left - radius}px;
                top: ${e.clientY - rect.top - radius}px;
                position: absolute;
                border-radius: 50%;
                background: rgba(26,16,8,0.15);
                transform: scale(0);
                animation: ripple 0.5s linear;
                pointer-events: none;
            `;

            const style = document.createElement('style');
            style.textContent = `@keyframes ripple{to{transform:scale(3);opacity:0}}`;
            if (!document.querySelector('#rippleStyle')) {
                style.id = 'rippleStyle';
                document.head.appendChild(style);
            }

            this.appendChild(circle);
            setTimeout(() => circle.remove(), 500);
        });
    });

    // Auto-dismiss alerts after 4 seconds
    document.querySelectorAll('.manga-alert').forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        }, 4000);
    });

    // Add ink-scratch texture class to panels on load
    document.querySelectorAll('.manga-panel').forEach(panel => {
        panel.classList.add('panel-loaded');
    });
});
