package com.example.utaste.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utaste.R;
import com.example.utaste.data.Recipe;
import com.example.utaste.data.RecipeIngredient;
import com.example.utaste.data.RecipeRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditRecipeActivity extends AppCompatActivity {

    private EditText editName, editDescription;
    private Spinner spinnerImage;
    private RecyclerView rvIngredients;
    private Button btnSave, btnCancel;

    private RecipeRepository repo;
    private Recipe recipe;
    private int recipeId;

    private IngredientDetailAdapter ingredientAdapter;
    private List<RecipeIngredient> ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        repo = RecipeRepository.getInstance();

        editName = findViewById(R.id.editRecipeName);
        editDescription = findViewById(R.id.editRecipeDescription);
        spinnerImage = findViewById(R.id.spinnerRecipeImageEdit);
        rvIngredients = findViewById(R.id.rvEditIngredients);
        btnSave = findViewById(R.id.btnSaveChanges);
        btnCancel = findViewById(R.id.btnCancelEdit);

        recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        if (recipeId == -1) {
            Toast.makeText(this, "Invalid Recipe", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recipe = repo.getRecipeById(recipeId);
        if (recipe == null) {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        editName.setText(recipe.getName());
        editDescription.setText(recipe.getDescription());

        // Dropdown with images from DB
        List<String> imageOptions = Arrays.asList("brownie", "cookies", "vanilla_cake");
        ArrayAdapter<String> imageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, imageOptions);
        imageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerImage.setAdapter(imageAdapter);

        int imageIndex = imageOptions.indexOf(recipe.getImage());
        if (imageIndex >= 0) spinnerImage.setSelection(imageIndex);

        ingredientList = new ArrayList<>(repo.getIngredientsForRecipe(recipeId));
        IngredientDetailAdapter adapter = new IngredientDetailAdapter(
                this,
                ingredientList,
                recipeId,
                false,  // readOnly = false
                false   // isCreateMode = false
        );
        rvIngredients.setAdapter(adapter);
        rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        rvIngredients.setNestedScrollingEnabled(false);




        btnSave.setOnClickListener(v -> saveChanges());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveChanges() {
        String newName = editName.getText().toString().trim();
        String newDesc = editDescription.getText().toString().trim();
        String newImage = spinnerImage.getSelectedItem().toString();

        if (newName.isEmpty() || newDesc.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int rows = repo.updateRecipe(recipeId, newName, newDesc, newImage);
        repo.clearIngredientsForRecipe(recipeId);
        for (RecipeIngredient ri : ingredientList) {
            repo.addIngredientToRecipe(recipeId, ri.getIngredientId(), ri.getQuantity());
        }

        if (rows > 0) {
            Toast.makeText(this, "Recipe updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}
