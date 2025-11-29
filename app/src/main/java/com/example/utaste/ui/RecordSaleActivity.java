package com.example.utaste.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.utaste.R;
import com.example.utaste.data.Recipe;
import com.example.utaste.data.RecipeRepository;
import com.example.utaste.data.SaleRepository;

import java.util.ArrayList;
import java.util.List;

public class RecordSaleActivity extends AppCompatActivity {

    private Spinner spinnerRecipes;
    private Spinner spinnerRating;
    private EditText etNote;
    private Button btnRecord;

    private RecipeRepository recipeRepository;
    private SaleRepository saleRepository;
    private List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_sale);

        spinnerRecipes = findViewById(R.id.spinnerRecipes);
        spinnerRating = findViewById(R.id.spinnerRating);
        etNote = findViewById(R.id.etNote);
        btnRecord = findViewById(R.id.btnRecordSaleConfirm);

        recipeRepository = RecipeRepository.getInstance();
        saleRepository = SaleRepository.getInstance();

        recipes = recipeRepository.listRecipes();
        List<String> names = new ArrayList<>();
        for (Recipe r : recipes) names.add(r.getName());

        ArrayAdapter<String> recipeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        recipeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRecipes.setAdapter(recipeAdapter);

        Integer[] ratings = new Integer[]{1,2,3,4,5};
        ArrayAdapter<Integer> ratingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ratings);
        ratingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRating.setAdapter(ratingAdapter);

        btnRecord.setOnClickListener(v -> recordSale());

        Button btnBack = findViewById(R.id.btnBackToWaiter);

        btnBack.setOnClickListener(v -> {
            Intent i = new Intent(RecordSaleActivity.this, WaiterActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });

    }

    private void recordSale() {
        int pos = spinnerRecipes.getSelectedItemPosition();
        if (pos < 0 || pos >= recipes.size()) {
            Toast.makeText(this, "Please select a recipe", Toast.LENGTH_SHORT).show();
            return;
        }
        int recipeId = recipes.get(pos).getId();
        int rating = (int) spinnerRating.getSelectedItem();
        String note = etNote.getText().toString().trim();

        long id = saleRepository.recordSale(recipeId, rating, note);
        if (id != -1) {
            Toast.makeText(this, "Sale recorded", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to record sale", Toast.LENGTH_SHORT).show();
        }
    }
}
