# ProgrammingQuiz Bidaoui 🚀

Une application Android interactive et complète pour tester et améliorer vos connaissances en programmation, avec un assistant IA intégré !

## 🌟 Fonctionnalités Principales

### Pour les Utilisateurs (Apprenants) :
*   **Authentification Sécurisée** : Inscription et connexion gérées via Firebase Authentication.
*   **Quiz de Programmation** : Testez vos connaissances avec des questions à choix multiples sur divers langages et concepts.
*   **Assistant Pédagogique IA (Chatbot)** : Un assistant intelligent intégré basé sur l'API Google Gemini, prêt à répondre à vos questions de code et vous aider à comprendre vos erreurs en temps réel !
*   **Résultats & Explications** : Évaluation immédiate de vos réponses et affichage des résultats en fin de partie (`ResultActivity`).
*   **Profil Utilisateur** : Suivi de vos informations et historique (`ProfileActivity`).
*   **Classement (Leaderboard)** : Comparez vos scores avec les autres joueurs de la plateforme (`LeaderboardActivity`).

### Pour les Administrateurs :
*   **Tableau de Bord Admin (Admin Dashboard)** : Interface dédiée pour gérer le contenu de l'application (`AdminDashboardActivity`).
*   **Gestion des Questions** : Ajout de nouvelles questions au format QCM pour enrichir la base de données du quiz (`AdminAddQuestionActivity`, `AdminQuestionsActivity`).
*   **Suivi des Utilisateurs** : Visualisez l'ensemble des utilisateurs inscrits sur la plateforme (`AdminUsersActivity`).

## 🛠️ Architecture Technologique

*   **Langage Principal** : Java (Android SDK)
*   **Interface Utilisateur** : Vues XML, Material Design, et intégration de la bibliothèque **Lottie** (6.0.0) pour des animations fluides et dynamiques.
*   **Base de données & Backend** : 
    *   **Firebase Authentication** : Gestion des utilisateurs.
    *   **Firebase Firestore** : Base de données NoSQL en temps réel pour stocker les questions, utilisateurs et scores.
    *   **Firebase Analytics & BOM** : Suivi des performances et événements.
*   **IA & API** : **Google Gemini API** intégrée nativement.
    *   **OkHttp3** (4.12.0) pour les requêtes HTTP.
    *   **Gson** (2.10.1) pour le parsing des réponses JSON.
*   **Version SDK** : Min SDK 24, Target SDK 37.

## 📂 Structure du Projet

L'architecture du code suit les standards MVC/MVP d'Android :
*   `activities/` : Contient les contrôleurs d'interface utilisateur (Ex: `MainActivity`, `QuizActivity`, `ChatActivity`, `AdminDashboardActivity`, etc.).
*   `models/` : Classes représentant les objets métiers (`Question`, `User`, `Score`, `ChatMessage`).
*   `adapters/` : Gestionnaires de données pour les listes et RecyclerViews (Ex: `ChatAdapter` pour la messagerie).
*   `firebase/` : Configuration et classes utilitaires liées aux services Firebase.
*   `utils/` : Classes utilitaires transverses de l'application.

## 🚀 Installation & Configuration

1.  **Cloner le dépôt**
    Ouvrez le projet `ProgrammingQuiz_Bidaoui` dans Android Studio.
2.  **Configuration de Firebase**
    *   Assurez-vous que le fichier de configuration `google-services.json` est bien présent dans le répertoire `app/`.
3.  **Configuration de l'API Gemini (Chatbot)**
    *   L'application utilise l'API Gemini pour le chatbot pédagogique. Pour des raisons de sécurité, la clé n'est pas codée en dur.
    *   Créez ou ouvrez le fichier `local.properties` situé à la racine du projet.
    *   Ajoutez-y votre clé d'API secrète de cette façon :
        ```properties
        GEMINI_API_KEY=votre_cle_api_secrete_ici
        ```
    *   Le script Gradle (`build.gradle.kts`) se chargera d'injecter cette clé sécuritairement lors de la compilation via `BuildConfig.GEMINI_API_KEY`.
4.  **Lancer l'application**
    Synchronisez le projet avec Gradle, compilez et exécutez sur un émulateur Android (API 24+) ou sur votre appareil physique.

## 👨‍💻 Développeur
Conçu et développé par **El Bidaoui**.
