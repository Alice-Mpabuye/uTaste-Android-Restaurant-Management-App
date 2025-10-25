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

import com.example.utaste.R;
import com.example.utaste.data.Ingredient;
import com.example.utaste.data.RecipeIngredient;
import com.example.utaste.data.UserDbHelper;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipeActivity extends AppCompatActivity {


    private EditText etName, etDescription;
    private Spinner spinnerImage;
    private Button btnSave, btnScanIngredient;
    private ActivityResultLauncher<ScanOptions> barLauncher;
    private UserDbHelper db;

    private RecyclerView rvIngredients;
    private List<RecipeIngredient> ingredientList;
    private IngredientAdapter adapter;

    private long currentRecipeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        db = new UserDbHelper(this);
        etName = findViewById(R.id.etRecipeName);
        etDescription = findViewById(R.id.etRecipeDescription);
        spinnerImage = findViewById(R.id.spinnerRecipeImage);

        btnSave = findViewById(R.id.btnSaveRecipe);
        btnScanIngredient = findViewById(R.id.btnScanIngredient);

        rvIngredients = findViewById(R.id.rvIngredients);
        ingredientList = new ArrayList<>();
        adapter = new IngredientAdapter(ingredientList);
        rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        rvIngredients.setAdapter(adapter);

        List<RecipeImage> imageList = new ArrayList<>();
        imageList.add(new RecipeImage("Brownie", R.drawable.brownie));
        imageList.add(new RecipeImage("Cookie", R.drawable.cookies));
        imageList.add(new RecipeImage("Vanilla cake", R.drawable.vanilla_cake));

        RecipeImageAdapter adapter_ = new RecipeImageAdapter(this, imageList);
        spinnerImage.setAdapter(adapter_);

        barLauncher = registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() != null) {
                String qrValue = result.getContents();
                Ingredient ing = db.getIngredientByQRCode(qrValue); // SQLite
                if (ing != null) {
                    showQuantityDialog(ing); // on demandera la quantité
                } else {
                    Toast.makeText(this, "Ingrédient inconnu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String desc = etDescription.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Le nom est obligatoire", Toast.LENGTH_SHORT).show();
                return;
            }

            RecipeImage selected = (RecipeImage) spinnerImage.getSelectedItem();
            String imageName = getResources().getResourceEntryName(selected.getResId());

            currentRecipeId = db.createRecipe(name, desc, imageName);

            if (currentRecipeId == -1) {
                for (RecipeIngredient ri : ingredientList) {
                    db.addIngredientToRecipe((int) currentRecipeId, ri.getIngredientId(), ri.getQuantity());
                }
                Toast.makeText(this, "Recette créée avec ingrédients ✅", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "⚠️ Le nom de recette doit être unique", Toast.LENGTH_LONG).show();
            }
        });

        btnScanIngredient.setOnClickListener(v -> ScanCode());
    }

    private void showQuantityDialog(Ingredient ingredient) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quantité (%) pour " + ingredient.getName());

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint("Ex: 20 pour 20%");
        builder.setView(input);

        builder.setPositiveButton("Ajouter", (dialog, which) -> {
            String text = input.getText().toString();
            if (text.isEmpty()) {
                Toast.makeText(this, "Veuillez entrer une quantité", Toast.LENGTH_SHORT).show();
                return;
            }

            double qty;
            try {
                qty = Double.parseDouble(text);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Quantité invalide", Toast.LENGTH_SHORT).show();
                return;
            }

            RecipeIngredient ri = new RecipeIngredient(ingredient.getId(), ingredient.getName(), qty);
            ingredientList.add(ri);
            adapter.notifyItemInserted(ingredientList.size() - 1);
        });

        builder.setNegativeButton("Annuler", (d, w) -> d.dismiss());
        builder.show();
    }

    private void ScanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scanner le QR Code");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }
}
