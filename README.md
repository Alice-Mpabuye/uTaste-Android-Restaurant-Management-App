# uTaste — Android Restaurant Management App

uTaste is an Android application developed in **Java** that allows users to manage recipes, nutritional information, and sales in a gourmet restaurant.  
The project was carried out **as a team** as part of the **SEG2505 – Software Design and Implementation** course at the University of Ottawa.

---

## 🚀 Features
- Role-based authentication (**Administrator**, **Chef**, **Waiter**)
- Recipe and ingredient management
- Add ingredients via **QR code scan**
- Retrieve nutritional information via **REST API (OpenFoodFacts)**
- Automatic calculation of nutritional balance (calories, carbohydrates, proteins, fats)
- Sales consultation and analysis
- Local **SQLite** database

---

## 👤 My Contributions
> Personal responsibilities in the project

- Modeling the application using **UML diagrams (PlantUML)**
- Implementation of **Chef** role features:
  - creation, modification, and deletion of recipes
  - addition, modification, and deletion of ingredients
  - retrieval of nutritional data via REST API
  - calculation of nutritional balance of recipes
- Implementation of **Waiter** role features:
  - consultation of recipes and their calorie balance

---


## 🧱 Architecture & Design
- Android application developed in **Java**
- Separation of responsibilities (UI, business logic, data)
- UML modeling including:
  - class diagrams
  - activity diagrams
  - sequence diagrams

The UML diagrams are available in the `/doc` folder.

---

## 🛠 Technologies
- Java  
- Android Studio  
- SQLite  
- REST APIs (JSON)  
- PlantUML  

### Validation scenario
1. Launch the Android Studio application on an emulator or physical device (API 30+).
2. Log in as:
   - **Admin:** `admin@local` / `admin-pwd`
   - **Chef:** `chef@local` / `chef-pwd`
