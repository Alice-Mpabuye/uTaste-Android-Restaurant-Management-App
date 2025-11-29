# Groupe-33-repo - Projet uTaste
Ceci est notre fichier README au format Markdown contenant des informations utiles dont :
- Une description de notre projet
- L’état courant du projet (fonctionnalité ajoutée)
- le processus de reconstruction
- Un scénario de validation de notre livrable
- Des limites éventuelles (anomalie, fonctionnalité manquante...)

## Première version de notre modèle conceptuel
![Diagramme V3](diagramV3.png)
Le fichier source PlantUML est disponible dans `doc/diagramV3.puml`.

## Description de notre projet
uTaste est une application Android développée en Java avec Android Studio dans le cadre du cours SEG2505 – Conception et implémentation logicielle à l’Université d’Ottawa. L’objectif est de créer une solution logicielle complète pour la gestion d’un restaurant gastronomique, intégrant des rôles utilisateurs : administrateur, chef cuisinier et serveur.

## État courant du projet
Les fonctionnalités principales implémentées depuis le dernier livrable pour chaque rôle sont ;

**Hors rôle :**
- Des tests unitaires automatisés de niveau API sont en place pour valider la gestion des utilisateurs
- Des tests unitaires automatisés de niveau API sont en place pour valider la gestion des ingrédients d’une recette.

**Administrateur :**

Rien a changé depuis le dernier livrable
- Réinitialiser la base de données 
- supprimer les vendeurs
- supprimer les recettes
- supprimer les ingrédients
- Réinitialiser le mot de passe d’un autre utilisateur
- Réinitialiser son propre mot de passe
 
**Chef :**
-  peut obtenir des informations nutritionnelles pertinentes sur chaque ingrédient
-  peut calculer le bilan calorique, en glucides, protides et lipides de la recette

**Serveur/Vendeur :**

Rien a changé depuis le dernier livrable
- S’authentifier
- Changer son propre mot de passe
- Se déconnecter
 
## Limites éventuelle
- Nécessite l'accès à l'internet
- Compatibilité peut être limité parce qu'on utilise Android (API ≥ 30) et pourrait rencontrer des problèmes sur d’autres versions.
- Il y a un seul administrateur et chef qui peut rendre à des limites logistiques (gestion de mot de passe limité)
- La base de donnée n'est pas synchronisée
- Les tests automatiques ne remplacent pas la validation manuelle

## Scénario de validation
1. Lancer l’application Android Studio sur un émulateur ou appareil physique (API 30+).  
2. Se connecter en tant que :
   - **Admin :** `admin@local` / `admin-pwd`
   - **Chef :** `chef@local` / `chef-pwd`
3. Tester les fonctionnalités accessibles selon le rôle :
   - Admin : voir les fonctionnalités au-dessus
   - Chef : voir les fonctionnalités au-dessus
      - Note pour l'ajout d'ingrédients lors de la création d'une recette: Va sur le site https://world.openfoodfacts.org/, recherche n'importe quel ingrédient et scan le code bar avec ton téléphone (disponible en cliquant sur "Add Ingredient" lors de la création d'une recette).
   - Waiter : voir les fonctionnalités au-dessus
4. Se déconnecter et répéter avec un autre rôle.


## Processus de reconstruction
1. Ouvrir Android Studio
2. Cloner le dépôt :
   ```bash
   git clone https://github.com/uOttawa-2025-2026-seg2505-projet/Groupe-33-repo.git
3. Ouvrir le dossier dans Android Studio, attendre pour les imports et Gradle sync et ensuite cliquer sur Run. 
