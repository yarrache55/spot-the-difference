# Spot The Difference - Spring MVC 

This project is a classic Spring MVC web application for a "Spot the Difference" game with authentication, leaderboard, and game save/load.

## Tech stack

- Java 17
- Spring Framework 6 (MVC)
- Spring Security 6
- Spring Data JPA + Hibernate
- Thymeleaf
- H2 database
- Maven
- Apache Tomcat 10+

## Project type

- Packaging: `war`
- Deployment: external servlet container (Tomcat)
- Boot usage: none

## Prerequisites

- JDK 17 installed
- Maven installed 
- Tomcat 10+ installed (because this project uses `jakarta.*`)

<<<<<<< HEAD

## Database

The app uses file-based H2 configured in `src/main/resources/application.properties`:
=======
## � Installation & Lancement

### Prérequis
- **Java 17+** (testé avec Java 25)
- **Maven 3.9+**

### Étapes
1. **Cloner le repo** :
   ```bash
   git clone https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git
   cd spot-the-difference
   ```

2. **Compiler & lancer** :
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. **Accéder au jeu** :
   - Ouvrir http://localhost:8080
   - S'inscrire / se connecter
   - Jouer !

### Fonctionnalités
- **Inscription/Connexion** sécurisée
- **3 niveaux** avec images manga
- **Système de score** et sauvegarde
- **Bouton indice** pour aider (gratuit)
- **Leaderboard** (à implémenter)
>>>>>>> 782d14b (Update README with setup instructions)

The database files are created in the working directory of Tomcat (or where the JVM starts).

<<<<<<< HEAD
## Default flow

1. Register a new account
2. Login
3. Play levels
4. Save/load progress
5. Check leaderboard
=======
## 🎨 Screenshots

*(Ajouter des captures d'écran ici)*

---

## 🤝 Contribution

1. Fork le repo
2. Créer une branche (`git checkout -b feature/AmazingFeature`)
3. Commit (`git commit -m 'Add some AmazingFeature'`)
4. Push (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

---

## 📄 Licence

Ce projet est sous licence MIT. Voir `LICENSE` pour plus de détails.

---

*Développé avec ❤️ pour le TP Architecture Web JEE*
    │   │   └── GameController.java
    │   ├── model/
    │   │   ├── Player.java        ← Entité JPA joueur
    │   │   ├── GameSave.java      ← Entité JPA sauvegarde
    │   │   ├── GameSession.java   ← Session HTTP (en mémoire)
    │   │   ├── Level.java         ← Config d'un niveau
    │   │   └── Difference.java    ← Zone différence avec détection clic
    │   ├── repository/
    │   │   ├── PlayerRepository.java
    │   │   └── GameSaveRepository.java
    │   └── service/
    │       ├── PlayerService.java  ← Inscription, auth, scores
    │       ├── LevelService.java   ← ⭐ Définition des niveaux et zones
    │       └── GameService.java    ← Logique de clic, sauvegarde/chargement
    └── resources/
        ├── application.properties
        ├── templates/
        │   ├── auth/login.html
        │   ├── auth/register.html
        │   └── game/
        │       ├── menu.html
        │       ├── play.html       ← Page de jeu principale
        │       ├── leaderboard.html
        │       └── victory.html
        └── static/
            ├── css/manga.css       ← Thème manga sketchbook
            ├── css/game.css        ← Styles spécifiques au jeu
            ├── js/manga.js         ← Effets UI globaux
            ├── js/game.js          ← Logique clic + timer + AJAX
            └── images/
                ├── README.md       ← 📸 Instructions pour tes images
                ├── level1/         ← Vide — place tes images ici !
                ├── level2/
                └── level3/
```
>>>>>>> 782d14b (Update README with setup instructions)

