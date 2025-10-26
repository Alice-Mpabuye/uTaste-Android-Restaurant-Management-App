package com.example.utaste.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utaste.R;
import com.example.utaste.data.Recipe;
import com.example.utaste.data.UserDbHelper;

import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        dbHelper = new UserDbHelper(this);
        recyclerView = findViewById(R.id.recyclerViewRecipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Recipe> recipeList = dbHelper.getAllRecipes();

        // We will create this adapter in the next step
        RecipeAdapter adapter = new RecipeAdapter(recipeList);
        recyclerView.setAdapter(adapter);
    }
}
