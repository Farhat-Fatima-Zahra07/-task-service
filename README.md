# TaskFlow — Gestionnaire de Tâches

**Auteurs :** Farhat Fatima Zahra & Elfahli Khadija  
**Module :** Ingénierie Logicielle Avancée — ISI_S6

---

## C'est quoi ?

Une application web pour gérer ses tâches :
- Créer, modifier, supprimer des tâches
- Filtrer par statut ou priorité
- Login et inscription
- Notifications pour les tâches urgentes

---

## Technologies

- **Backend :** Java + Spring Boot
- **Frontend :** HTML, CSS, JavaScript
- **Base de données :** H2
- **Tests :** JUnit 5
- **Docker :** oui
- **CI/CD :** GitHub Actions

---

## Comment lancer

**Dans IntelliJ :**
1. Ouvrir `TaskServiceApplication.java`
2. Cliquer ▶
3. Aller sur `http://localhost:8090`

**Avec Docker :**
```bash
docker-compose up --build
```

**Lancer les tests :**
```bash
mvn test
```

---

## Design Patterns utilisés

**Strategy** → pour filtrer les tâches par statut ou priorité  
**Decorator** → pour ajouter des badges et notifications sur les tâches

---

## Les pages

| URL | Description |
|---|---|
| http://localhost:8090 | Application principale |
| http://localhost:8090/h2-console | Base de données |

---

## Les endpoints principaux

 Méthode | URL | Description 
|--|--|--|
 POST | /api/auth/register | Inscription |
| POST | /api/auth/login | Connexion |
| GET | /api/tasks?userId=1 | Liste des tâches |
| POST | /api/tasks?userId=1 | Créer une tâche |
| PUT | /api/tasks/{id} | Modifier |
| DELETE | /api/tasks/{id} | Supprimer |
| GET | /api/tasks/urgent?userId=1 | Tâches urgentes |