package com.example.utaste.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utaste.R;
import com.example.utaste.data.Recipe;
import com.example.utaste.data.RecipeIngredient;
import com.example.utaste.data.RecipeRepository;

import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeClickListener {

    private RecyclerView recyclerView;
    private TextView textViewEmpty;
    private Button btnAddRecipe, btnGoToChef;
    private RecipeRepository recipeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        recipeRepository = RecipeRepository.getInstance();

        recyclerView = findViewById(R.id.recyclerViewRecipes);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        btnAddRecipe = findViewById(R.id.btnAddRecipe);
        btnGoToChef = findViewById(R.id.btnGoToChef);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAddRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeListActivity.this, CreateRecipeActivity.class);
            startActivity(intent);
        });

        btnGoToChef.setOnClickListener(v -> finish());

        updateRecipeList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRecipeList();
    }

    private void updateRecipeList() {
        List<Recipe> recipeList = recipeRepository.listRecipes();

        if (recipeList == null || recipeList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            textViewEmpty.setVisibility(View.VISIBLE);
            btnAddRecipe.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            textViewEmpty.setVisibility(View.GONE);
            btnAddRecipe.setVisibility(View.GONE);
            RecipeAdapter adapter = new RecipeAdapter(recipeList, this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_recipe_detail, null);
        builder.setView(dialogView);

        ImageView recipeImage = dialogView.findViewById(R.id.dialog_recipe_image);
        RecyclerView ingredientsRecyclerView = dialogView.findViewById(R.id.dialog_ingredients_recyclerview);

        builder.setTitle(recipe.getName());

        int imageResId = getResources().getIdentifier(recipe.getImage(), "drawable", getPackageName());
        if (imageResId != 0) recipeImage.setImageResource(imageResId);

        List<RecipeIngredient> ingredients = recipeRepository.getIngredientsForRecipe(recipe.getId());
        IngredientDetailAdapter adapter = new IngredientDetailAdapter(
                this,
                ingredients,
                recipe.getId(),
                true,  // readOnly = true
                false  // isCreateMode = false
        );
        ingredientsRecyclerView.setAdapter(adapter);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView calorieRecyclerView = dialogView.findViewById(R.id.rvViewCalories);

        double totalFat = 0.0;
        double totalCarbs = 0.0;
        double totalProtein = 0.0;

        for (RecipeIngredient ingredient : ingredients) {
            double quantityFactor = ingredient.getQuantity() / 100.0;
            totalFat += ingredient.getFat() * quantityFactor;
            totalCarbs += ingredient.getCarbs() * quantityFactor; // Corrected method name
            totalProtein += ingredient.getProtein() * quantityFactor;
        }
        double totalCalories = (totalFat * 9) + (totalCarbs * 4) + (totalProtein * 4);

        List<String> nutritionFacts = new ArrayList<>();
        nutritionFacts.add(String.format("Calories: %.1f kcal", totalCalories));
        nutritionFacts.add(String.format("Lipides: %.1f g", totalFat));
        nutritionFacts.add(String.format("Glucides: %.1f g", totalCarbs));
        nutritionFacts.add(String.format("Protein: %.1f g", totalProtein));

        TextAdapter nutritionAdapter = new TextAdapter(nutritionFacts);
        calorieRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        calorieRecyclerView.setAdapter(nutritionAdapter);


        builder.setPositiveButton("Edit", (dialog, which) -> {
            Intent intent = new Intent(RecipeListActivity.this, CreateRecipeActivity.class);
            intent.putExtra("RECIPE_ID", recipe.getId());
            startActivity(intent);
        });

        builder.setNegativeButton("Delete", (dialog, which) -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Recipe")
                    .setMessage("Are you sure you want to delete this recipe?")
                    .setPositiveButton("Yes", (d, w) -> {
                        recipeRepository.deleteRecipe(recipe.getId());
                        Toast.makeText(this, "Recipe deleted", Toast.LENGTH_SHORT).show();
                        updateRecipeList();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        builder.setNeutralButton("Close", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
