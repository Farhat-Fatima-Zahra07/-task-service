# TaskFlow — Gestionnaire de Tâches

**Auteurs :** Farhat Fatima Zahra & Elfahli Khadija
**Module :** Ingénierie Logicielle Avancée — ISI_S6



## C'est quoi ?

TaskFlow est une application web permettant de gérer des tâches facilement.

Fonctionnalités :

* Inscription et connexion
* Ajouter une tâche
* Modifier une tâche
* Supprimer une tâche
* Filtrer les tâches
* Afficher les tâches urgentes



## Technologies utilisées

* Java 17
* Spring Boot 3
* HTML, CSS, JavaScript
* Base de données H2
* JUnit 5
* Docker
* GitHub Actions



## Comment lancer le projet

### Dans IntelliJ

1. Ouvrir le projet
2. Exécuter `TaskServiceApplication.java`
3. Aller sur :

```text
http://localhost:8090
```

### Avec Docker

```bash
docker-compose up --build
```

### Exécuter les tests

```bash
mvn test
```



## Design Patterns utilisés

**Strategy**

* Filtrage des tâches par statut
* Filtrage des tâches par priorité

**Decorator**

* Ajout de badges de priorité
* Ajout de notifications pour les tâches urgentes



## Tests et CI/CD

* 12 tests unitaires JUnit 5
* BUILD SUCCESSFUL
* Pipeline GitHub Actions opérationnelle
* Construction automatique du JAR et de l'image Docker



## Pages disponibles

| URL                              | Description        |
| -------------------------------- | ------------------ |
| http://localhost:8090            | Application        |
| http://localhost:8090/h2-console | Base de données H2 |



## Endpoints principaux

| Méthode | URL                        | Description         |
| ------- | -------------------------- | ------------------- |
| POST    | /api/auth/register         | Inscription         |
| POST    | /api/auth/login            | Connexion           |
| GET     | /api/tasks?userId=1        | Liste des tâches    |
| POST    | /api/tasks?userId=1        | Ajouter une tâche   |
| PUT     | /api/tasks/{id}            | Modifier une tâche  |
| DELETE  | /api/tasks/{id}            | Supprimer une tâche |
| GET     | /api/tasks/urgent?userId=1 | Tâches urgentes     |



## Résultat

* Application fonctionnelle
* Design Patterns implémentés
* 12 tests validés
* Docker opérationnel
* CI/CD GitHub Actions fonctionnelle
* Projet disponible sur GitHub
