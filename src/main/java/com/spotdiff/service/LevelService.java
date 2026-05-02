package com.spotdiff.service;

import com.spotdiff.model.Difference;
import com.spotdiff.model.Level;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Defines all game levels.
 *
 * HOW TO ADD YOUR IMAGES:
 * ─────────────────────────────────────────────────────────────────────
 * 1. Place your image pairs in:
 * src/main/resources/static/images/level1/ (original.png + modified.png)
 * src/main/resources/static/images/level2/ (original.png + modified.png)
 * src/main/resources/static/images/level3/ (original.png + modified.png)
 *
 * 2. Update the Difference zones below for each level.
 * Coordinates are PERCENTAGES (0–100) of image width/height.
 * Use browser DevTools → pick element → compute % from pixel coords.
 * radiusPercent = click tolerance (5–8 is comfortable, 3–4 is hard).
 *
 * DIFFICULTY PROGRESSION:
 * Level 1 – 3 differences, large zones (radius 7%), 120 sec limit
 * Level 2 – 5 differences, medium zones (radius 5%), 90 sec limit
 * Level 3 – 7 differences, small zones (radius 3.5%), 60 sec limit
 * ─────────────────────────────────────────────────────────────────────
 */
@Service
public class LevelService {

    private final List<Level> levels;

    public LevelService() {
        this.levels = new ArrayList<>();
        initLevels();
    }

    private void initLevels() {

        // ── LEVEL 1 ── 6 exact rectangle differences for nana images ──
        List<Difference> diffs1 = Arrays.asList(
                new Difference("L1-D1", 282, 230, 358, 276, 800, 800),
                new Difference("L1-D2", 212, 174, 288, 210, 800, 800),
                new Difference("L1-D3", 325, 173, 352, 204, 800, 800),
                new Difference("L1-D4", 512, 262, 699, 331, 800, 800),
                new Difference("L1-D5", 286, 493, 411, 636, 800, 800),
                new Difference("L1-D6", 652, 464, 796, 535, 800, 800));

        levels.add(new Level(
                1,
                "Première Vision",
                "Niveau facile — 6 différences à découvrir dans l'image nana.",
                "/images/level1/nana1.png",
                "/images/level1/nana2.png",
                diffs1,
                150, // 2.5 minutes
                200 // points per difference
        ));

        // ── LEVEL 2 ── 16 exact rectangle differences for the green pic ──
        List<Difference> diffs2 = Arrays.asList(
                new Difference("L2-D1", 220, 6, 427, 47, 800, 600),
                new Difference("L2-D2", 496, 283, 584, 317, 800, 600),
                new Difference("L2-D3", 154, 48, 196, 69, 800, 600),
                new Difference("L2-D4", 255, 54, 306, 111, 800, 600),
                new Difference("L2-D5", 632, 30, 682, 119, 800, 600),
                new Difference("L2-D6", 642, 275, 681, 355, 800, 600),
                new Difference("L2-D7", 740, 7, 784, 61, 800, 600),
                new Difference("L2-D8", 716, 256, 785, 306, 800, 600),
                new Difference("L2-D9", 501, 52, 536, 83, 800, 600),
                new Difference("L2-D10", 555, 77, 580, 99, 800, 600),
                new Difference("L2-D11", 561, 376, 643, 421, 800, 600),
                new Difference("L2-D12", 310, 505, 377, 536, 800, 600),
                new Difference("L2-D13", 269, 361, 207, 307, 800, 600),
                new Difference("L2-D14", 117, 221, 148, 254, 800, 600),
                new Difference("L2-D15", 182, 213, 255, 302, 800, 600),
                new Difference("L2-D16", 736, 537, 790, 591, 800, 600));

        levels.add(new Level(
                2,
                "Regard Aiguisé",
                "Niveau intermédiaire — 16 différences à repérer dans la version verte.",
                "/images/level2/pic1.png",
                "/images/level2/pic2.png",
                diffs2,
                180, // 3 minutes
                250));

        // ── LEVEL 3 ── 10 exact rectangle differences for the flowery pic ──
        List<Difference> diffs3 = Arrays.asList(
                new Difference("L3-D1", 354, 192, 455, 273, 800, 1039),
                new Difference("L3-D2", 651, 115, 735, 407, 800, 1039),
                new Difference("L3-D3", 576, 172, 646, 296, 800, 1039),
                new Difference("L3-D4", 10, 352, 86, 534, 800, 1039),
                new Difference("L3-D5", 382, 894, 540, 1008, 800, 1039),
                new Difference("L3-D6", 323, 650, 392, 714, 800, 1039),
                new Difference("L3-D7", 168, 770, 250, 825, 800, 1039),
                new Difference("L3-D8", 763, 140, 804, 216, 800, 1039),
                new Difference("L3-D9", 711, 916, 797, 1034, 800, 1039),
                new Difference("L3-D10", 104, 336, 176, 536, 800, 1039));

        levels.add(new Level(
                3,
                "L'Œil du Maître",
                "Niveau expert — 10 différences très détaillées dans l'image fleurie.",
                "/images/level3/pic21.png",
                "/images/level3/pic22.png",
                diffs3,
                120, // 2 minutes
                300));
    }

    public List<Level> getAllLevels() {
        return levels;
    }

    public Optional<Level> getLevelByNumber(int number) {
        return levels.stream()
                .filter(l -> l.getNumber() == number)
                .findFirst();
    }

    public int getTotalLevels() {
        return levels.size();
    }

    public boolean isLastLevel(int levelNumber) {
        return levelNumber >= levels.size();
    }
}
