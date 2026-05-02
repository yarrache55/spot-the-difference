# 📸 Dossier Images — Instructions

Ce dossier contient les images du jeu **Spot the Difference**.
Les sous-dossiers sont intentionnellement vides — c'est à toi d'y placer tes images !

---

## 📁 Structure attendue

```
images/
├── level1/
│   ├── original.jpg    ← Image originale du niveau 1
│   └── modified.jpg    ← Image avec les différences (niveau 1)
├── level2/
│   ├── original.jpg
│   └── modified.jpg
└── level3/
    ├── original.jpg
    └── modified.jpg
```

---

## 🎨 Conseils pour les images

- **Format** : JPG, PNG ou WEBP (le `.jpg` est utilisé par défaut dans le code)
- **Résolution recommandée** : 800×600 px minimum pour une bonne lisibilité
- **Style conseillé** : Manga, sketchbook, noir et blanc ou couleurs encre
- **Ratio** : Garde le même ratio pour les deux images d'une même paire !
- Les deux images doivent être **exactement de la même taille**

---

## 🎯 Configuration des zones de différences

Après avoir placé tes images, tu dois mettre à jour les coordonnées
des différences dans le fichier :

```
src/main/java/com/spotdiff/service/LevelService.java
```

Les coordonnées sont en **pourcentage** de la largeur/hauteur de l'image :
- `xPercent` = position horizontale du centre de la zone (0 = gauche, 100 = droite)
- `yPercent` = position verticale du centre de la zone (0 = haut, 100 = bas)
- `radiusPercent` = rayon de tolérance du clic (7 = facile, 5 = moyen, 3.5 = difficile)

### Comment trouver les coordonnées ?

1. Ouvre ton image dans le navigateur (ou dans un éditeur)
2. Note les pixels X,Y du centre de la différence
3. Divise par la largeur/hauteur totale × 100 pour obtenir le %

Exemple : différence à pixel (400, 300) dans une image 800×600 px
→ xPercent = 400/800 × 100 = **50.0**
→ yPercent = 300/600 × 100 = **50.0**

---

## ✅ Checklist avant de lancer le jeu

- [ ] `level1/original.jpg` et `level1/modified.jpg` présents
- [ ] `level2/original.jpg` et `level2/modified.jpg` présents
- [ ] `level3/original.jpg` et `level3/modified.jpg` présents
- [ ] Coordonnées des différences mises à jour dans `LevelService.java`
- [ ] Construire avec `mvn clean package` puis deployer le `.war` sur Tomcat
- [ ] Ouvrir http://localhost:8080

---

Bon jeu ! 👁
