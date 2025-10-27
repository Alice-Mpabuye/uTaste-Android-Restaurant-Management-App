# Groupe-33-repo - Projet uTaste


Ceci est notre fichier README au format Markdown contenant des informations utiles dont :
- Une description de notre projet
- L’état courant du projet (fonctionnalité ajoutée)
- le processus de reconstruction
- Un scénario de validation de notre livrable
- Des limites éventuelles (anomalie, fonctionnalité manquante...)


## Première version de notre modèle conceptuel
![Diagramme conceptuel](diagramV1.png)
Le fichier source PlantUML est disponible dans `doc/diagramV1.puml`.


## Description de notre projet
uTaste est une application Android développée en Java avec Android Studio dans le cadre du cours SEG2505 – Conception et implémentation logicielle à l’Université d’Ottawa. L’objectif est de créer une solution logicielle complète pour la gestion d’un restaurant gastronomique, intégrant des rôles utilisateurs : administrateur, chef cuisinier et serveur.


## État courant du projet
Les fonctionnalités principales implémentées depuis le dernier livrable pour chaque rôle sont ;


**Administrateur**
- Réinitialiser la base de données 
- supprimer les vendeurs
- supprimer les recettes
- supprimer les ingrédients
- Réinitialiser le mot de passe d’un autre utilisateur
- Réinitialiser son propre mot de passe
 
**Chef**
- Réinitialiser son propre mot de passe
- Créer, modifier ou supprimer une recette
- Ajouter un ingrédient à la recette en :
  - Scannant le QR-code d’un produit disponible en épicerie
- Modifier/Supprimer un ingrédient d’une recette 


**Serveur/Vendeur**


Rien a changé depuis le dernier livrable
- S’authentifier
- Changer son propre mot de passe
- Se déconnecter
 
## Limites éventuelle
- Nécessite l'accès à l'internet
- Compatibilité peut être limité parce qu'on utilise Android (API ≥ 30) et pourrait rencontrer des problèmes sur d’autres versions.
- Il y a un seul administrateur et chef qui peut rendre à des limites logistiques (gestion de mot de passe limité)
- La base de donnée n'est pas synchronisée


## Scénario de validation


1. Lancer l’application Android Studio sur un émulateur ou appareil physique (API 30+).  
2. Se connecter en tant que :
   - **Admin :** `admin@local` / `admin-pwd`
   - **Chef :** `chef@local` / `chef-pwd`
3. Tester les fonctionnalités accessibles selon le rôle :
   - Admin : voir les fonctionnalités au-dessus
   - Chef : voir les fonctionnalités au-dessus
   - Waiter : voir les fonctionnalités au-dessus
4. Se déconnecter et répéter avec un autre rôle.


## Processus de reconstruction


1. Ouvrir Android Studio
2. Cloner le dépôt :
   ```bash
   git clone https://github.com/uOttawa-SEG2505/Groupe-33-repo.git
