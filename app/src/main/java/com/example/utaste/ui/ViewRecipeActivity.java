package com.example.utaste.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.utaste.R;
import com.example.utaste.data.Recipe;
import com.example.utaste.data.RecipeIngredient;
import com.example.utaste.data.RecipeRepository;

import java.util.ArrayList;
import java.util.List;

public class ViewRecipeActivity extends AppCompatActivity {

    private TextView tvRecipeName, tvRecipeDescription;
    private ImageView ivRecipeImage;
    private RecyclerView rvIngredients;

    private int recipeId;
    private List<RecipeIngredient> ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        tvRecipeName = findViewById(R.id.tvRecipeName);
        tvRecipeDescription = findViewById(R.id.tvRecipeDescription);
        ivRecipeImage = findViewById(R.id.ivRecipeImage);
        rvIngredients = findViewById(R.id.rvViewIngredients);


        recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        if (recipeId != -1) {
            loadRecipeData();
        }
    }

    private void loadRecipeData() {
        RecipeRepository repo = RecipeRepository.getInstance();
        Recipe recipe = repo.getRecipeById(recipeId);
        if (recipe != null) {
            tvRecipeName.setText(recipe.getName());
            tvRecipeDescription.setText(recipe.getDescription());

            // Load image from resources
            int imageResId = getResources().getIdentifier(recipe.getImage(), "drawable", getPackageName());
            ivRecipeImage.setImageResource(imageResId);

            // Load ingredients
            ingredientList = repo.getIngredientsForRecipe(recipeId);

            IngredientDetailAdapter adapter = new IngredientDetailAdapter(
                    this,
                    ingredientList,
                    recipeId,
                    true,   // readOnly = true
                    false   // isCreateMode = false
            );

            rvIngredients.setAdapter(adapter);
            rvIngredients.setLayoutManager(new LinearLayoutManager(this));
            rvIngredients.setNestedScrollingEnabled(false);


        }
    }
}
