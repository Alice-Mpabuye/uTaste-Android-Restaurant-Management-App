package com.example.utaste.ui;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.utaste.R;
import com.example.utaste.data.Ingredient;
import com.example.utaste.data.Recipe;
import com.example.utaste.data.RecipeIngredient;
import com.example.utaste.data.RecipeRepository;
import com.example.utaste.data.UserDbHelper;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipeActivity extends AppCompatActivity {

    private EditText etName, etDescription;
    private Spinner spinnerImage;
    private Button btnSave, btnScanIngredient, btnGoToChefFromCreate;

    private UserDbHelper dbHelper;
    private RecipeRepository recipeRepository;

    private RecyclerView rvIngredients;
    private List<RecipeIngredient> ingredientList;
    private IngredientDetailAdapter adapter;

    private long currentRecipeId = -1;
    private boolean isEditMode = false;
    private ActivityResultLauncher<ScanOptions> barLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        dbHelper = new UserDbHelper(this);
        recipeRepository = RecipeRepository.getInstance();

        etName = findViewById(R.id.etRecipeName);
        etDescription = findViewById(R.id.etRecipeDescription);
        spinnerImage = findViewById(R.id.spinnerRecipeImage);

        btnSave = findViewById(R.id.btnSaveRecipe);
        btnScanIngredient = findViewById(R.id.btnScanIngredient);
        btnGoToChefFromCreate = findViewById(R.id.btnGoToChefFromCreate);

        rvIngredients = findViewById(R.id.rvIngredients);
        ingredientList = new ArrayList<>();

        adapter = new IngredientDetailAdapter(
                this,
                ingredientList,
                -1,     // recipeId not yet saved
                false,  // readOnly = false
                true    // isCreateMode = true
        );
        rvIngredients.setAdapter(adapter);
        rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        rvIngredients.setNestedScrollingEnabled(false);


        // Spinner setup (you might have your own adapter for images)
        List<RecipeImage> imageList = new ArrayList<>();
        imageList.add(new RecipeImage("Brownie", R.drawable.brownie));
        imageList.add(new RecipeImage("Cookie", R.drawable.cookies));
        imageList.add(new RecipeImage("Vanilla cake", R.drawable.vanilla_cake));

        RecipeImageAdapter imageAdapter = new RecipeImageAdapter(this, imageList);
        spinnerImage.setAdapter(imageAdapter);

        // If editing an existing recipe
        if (getIntent().hasExtra("RECIPE_ID")) {
            isEditMode = true;
            currentRecipeId = getIntent().getIntExtra("RECIPE_ID", -1);
            loadRecipeData();
            btnSave.setText("Update Recipe");
        }

        // Barcode scanner setup
        barLauncher = registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() != null) {
                String qrValue = result.getContents();
                Ingredient ing = dbHelper.getIngredientByQRCode(qrValue);
                if (ing != null) {
                    fetchNutritionFromOpenFoodFacts(ing.getQrCode(), ing);
                } else {
                    Ingredient newIngredient = new Ingredient(0, "Unknown ingredient", qrValue);
                    fetchNutritionFromOpenFoodFacts(qrValue, newIngredient);
                }
            }
        });

        btnSave.setOnClickListener(v -> saveOrUpdateRecipe());
        btnScanIngredient.setOnClickListener(v -> scanCode());
        btnGoToChefFromCreate.setOnClickListener(v -> finish());
    }

    private void loadRecipeData() {
        Recipe recipe = recipeRepository.getRecipeById((int) currentRecipeId);
        if (recipe != null) {
            etName.setText(recipe.getName());
            etDescription.setText(recipe.getDescription());
            ingredientList.addAll(recipeRepository.getIngredientsForRecipe((int) currentRecipeId));
            adapter.notifyDataSetChanged();

            String imageName = recipe.getImage();
            for (int i = 0; i < spinnerImage.getCount(); i++) {
                RecipeImage item = (RecipeImage) spinnerImage.getItemAtPosition(i);
                if (item.getName().equals(imageName)) {
                    spinnerImage.setSelection(i);
                    break;
                }
            }
        }
    }

    private void saveOrUpdateRecipe() {
        String name = etName.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        RecipeImage selected = (RecipeImage) spinnerImage.getSelectedItem();
        String imageName = getResources().getResourceEntryName(selected.getResId());

        if (name.isEmpty()) {
            Toast.makeText(this, "Recipe name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEditMode) {
            recipeRepository.updateRecipe((int) currentRecipeId, name, desc, imageName);
            recipeRepository.clearIngredientsForRecipe((int) currentRecipeId);
        } else {
            currentRecipeId = recipeRepository.createRecipe(name, desc, imageName);
        }

        if (currentRecipeId != -1) {
            for (RecipeIngredient ri : ingredientList) {
                recipeRepository.addIngredientToRecipe((int) currentRecipeId, ri.getIngredientId(), ri.getQuantity());
            }
            Toast.makeText(this, isEditMode ? "Recipe updated ✅" : "Recipe created ✅", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error: Recipe name might already exist.", Toast.LENGTH_LONG).show();
        }
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan the QR Code");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    private void fetchNutritionFromOpenFoodFacts(String barcode, Ingredient ingredient) {
        String url = "https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getInt("status") == 1) {
                            JSONObject product = response.getJSONObject("product");

                            String productName = product.optString("product_name", "Unknown ingredient");
                            ingredient.setName(productName);

                            JSONObject nutriments = product.getJSONObject("nutriments");
                            StringBuilder info = new StringBuilder();

                            if (nutriments.has("carbohydrates_100g"))
                                info.append("Carbohydrates for 100g: ")
                                        .append(nutriments.getDouble("carbohydrates_100g")).append("g\n");

                            if (nutriments.has("fat_100g"))
                                info.append("Fat for 100g: ")
                                        .append(nutriments.getDouble("fat_100g")).append("g\n");

                            if (nutriments.has("fiber_100g"))
                                info.append("Fiber for 100g: ")
                                        .append(nutriments.getDouble("fiber_100g")).append("g\n");

                            if (nutriments.has("proteins_100g"))
                                info.append("Proteins for 100g: ")
                                        .append(nutriments.getDouble("proteins_100g")).append("g\n");

                            if (nutriments.has("salt_100g"))
                                info.append("Salt for 100g: ")
                                        .append(nutriments.getDouble("salt_100g")).append("g\n");

                            new AlertDialog.Builder(this)
                                    .setTitle("Ingredient: " + ingredient.getName())
                                    .setMessage(info.toString())
                                    .setPositiveButton("Add to Recipe", (dialog, which) -> showQuantityDialog(ingredient))
                                    .setNegativeButton("Cancel", null)
                                    .show();

                        } else {
                            Toast.makeText(this, "Ingredient not found on OpenFoodFacts", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }

    private void showQuantityDialog(Ingredient ingredient) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quantity (%) for " + ingredient.getName());

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint("Ex: 20 for 20%");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String text = input.getText().toString();
            if (text.isEmpty()) {
                Toast.makeText(this, "Please enter a quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            double qty;
            try {
                qty = Double.parseDouble(text);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            // --- INSERT NEW INGREDIENT IF NEEDED ---
            if (ingredient.getId() == 0) {
                long newId = recipeRepository.insertIngredient(ingredient);
                if (newId != -1) {
                    ingredient.setId((int) newId);
                } else {
                    // Ingredient already exists, fetch its ID
                    Ingredient existing = dbHelper.getIngredientByName(ingredient.getName());
                    if (existing != null) ingredient.setId(existing.getId());
                }
            }

            RecipeIngredient ri = new RecipeIngredient(ingredient.getId(), ingredient.getName(), qty);
            ingredientList.add(ri);
            adapter.notifyItemInserted(ingredientList.size() - 1);
        });

        builder.setNegativeButton("Cancel", (d, w) -> d.dismiss());
        builder.show();
    }

}
