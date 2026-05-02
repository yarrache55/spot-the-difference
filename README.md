# Spot The Difference - Spring MVC 

This project is a Spring MVC web application for a "Spot the Difference" game with authentication, leaderboard, and game save/load.

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



## Database

The app uses file-based H2 configured in `src/main/resources/application.properties`


### Fonctionnalités
- **Inscription/Connexion** sécurisée
- **3 niveaux** avec images manga de pinterest
- **Système de score** et sauvegarde
- **Bouton indice** pour aider 
- **Leaderboard** (à implémenter)


The database files are created in the working directory of Tomcat (or where the JVM starts).

## Default flow

1. Register a new account
2. Login
3. Play levels
4. Save/load progress
5. Check leaderboard




*Développé pour le TP Architecture Web JEE*
2026

