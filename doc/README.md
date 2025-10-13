# Groupe-33-repo

Ceci est notre fichier README au format Markdown contenant des informations utiles dont : 
- une description de notre projet
- éventuellement :
  - des instructions de reconstruction
  - un scénario de validation de notre livrable
- des limites éventuelles (anomalie, fonctionnalité manquante...)

## Première version de notre modèle conceptuel
![Alt text](c:\Users\paule\OneDrive\Documents\Groupe-33-repo\Groupe-33-repo\out\DiagrammeV1\DiagrammeV1.png)

## Description de notre projet
uTaste est une application Android développée en Java avec Android Studio dans le cadre du cours SEG2505 – Conception et implémentation logicielle à l’Université d’Ottawa. L’objectif est de créer une solution logicielle complète pour la gestion d’un restaurant gastronomique, intégrant des rôles utilisateurs : administrateur, chef cuisinier et serveur.

### Les fonctionnalités principales pour chaque rôles sont
**Administrateur**
- s’authentifier 
- changer son propre mot de passe 
- changer son profil (prénom, nom…) ou celui d’un autre utilisateur 
- créer des utilisateurs avec le rôle « serveur » 
- réinitialiser la base de données 
- réinitialiser le mot de passe d’un autre utilisateur 
- se déconnecter
  
**Chef**
- s’authentifier 
- changer son propre mot de passe 
- créer, modifier ou supprimer une recette 
- ajouter un ingrédient à la recette en :
  - scannant le QR-code d’un produit disponible en épicerie 
  - précisant la quantité utilisée (en pourcentage de l’ingrédient choisi) 
- modifier la quantité d’un ingrédient d’une recette 
- supprimer un ingrédient d’une recette 
- obtenir des informations nutritionnelles pertinentes pour chaque ingrédient, telles que celles fournies par OpenFoodFacts, en utilisant l’identifiant obtenu au moyen du code QR 
- calculer le bilan calorique, en glucides, protides et lipides de la recette 
- se déconnecter
  
**Serveur**
- s’authentifier 
- changer son propre mot de passe 
- voir la liste des recettes et leur bilan calorique 
- enregistrer une vente avec une note et une appréciation 
- avoir un bilan des ventes contenant 
  - la liste des recettes vendues et le nombre de vente pour chaque recette 
  - la note moyenne de chaque recette
- se déconnecter
  
## limites éventuelle
- nécessite l'accès à l'internet
- Compatibilité peut être limité parce qu'on utilise Android (API ≥ 30) et pourrait rencontrer des problèmes sur d’autres versions.
- il y a un seul administrateur et chef qui peut rendre à des limites logistiques