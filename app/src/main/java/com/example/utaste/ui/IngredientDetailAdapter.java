package com.example.utaste.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utaste.data.RecipeIngredient;
import com.example.utaste.data.RecipeRepository;

import java.util.List;

public class IngredientDetailAdapter extends RecyclerView.Adapter<IngredientDetailAdapter.ViewHolder> {

    private final Context context;
    private final List<RecipeIngredient> ingredients;
    private final int recipeId;
    private final boolean isCreateMode;
    private final boolean readOnly; // true when in view mode, false when in edit/create mode

    public IngredientDetailAdapter(Context context, List<RecipeIngredient> ingredients, int recipeId, boolean readOnly, boolean isCreateMode) {
        this.context = context;
        this.ingredients = ingredients;
        this.recipeId = recipeId;
        this.readOnly = readOnly;
        this.isCreateMode = isCreateMode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeIngredient ingredient = ingredients.get(position);

        holder.text1.setText(ingredient.getName());
        holder.text2.setText(String.format("%.2f %%", ingredient.getQuantity()));

        // Remove previous listener
        holder.itemView.setOnClickListener(null);

        // Clickable only if creating OR editing
        if (isCreateMode || !readOnly) {
            holder.itemView.setOnClickListener(v -> showIngredientOptions(position, ingredient));
        }
    }

    private void showIngredientOptions(int position, RecipeIngredient ingredient) {
        new AlertDialog.Builder(context)
                .setTitle("Modify or Delete Ingredient")
                .setMessage("What would you like to do with " + ingredient.getName() + "?")
                .setPositiveButton("Modify Quantity", (dialog, which) -> showQuantityDialog(position, ingredient))
                .setNegativeButton("Delete Ingredient", (dialog, which) -> deleteIngredient(position))
                .show();
    }

    private void showQuantityDialog(int position, RecipeIngredient ingredient) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Quantity for " + ingredient.getName());

        EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setText(String.valueOf(ingredient.getQuantity()));
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            try {
                double newQty = Double.parseDouble(input.getText().toString());
                if (newQty < 0) {
                    Toast.makeText(context, "Quantity must be positive", Toast.LENGTH_SHORT).show();
                    return;
                }

                RecipeIngredient updatedIngredient = new RecipeIngredient(
                        ingredient.getIngredientId(),
                        ingredient.getName(),
                        newQty,
                        ingredient.getCarbs(),
                        ingredient.getProtein(),
                        ingredient.getFat(),
                        ingredient.getFiber(),
                        ingredient.getSalt()
                );
                ingredients.set(position, updatedIngredient);
                notifyItemChanged(position);

                if (!isCreateMode) updateIngredientsInRepo();

                Toast.makeText(context, "Quantity updated", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Invalid number", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteIngredient(int position) {
        ingredients.remove(position);
        notifyItemRemoved(position);

        if (!isCreateMode) updateIngredientsInRepo();

        Toast.makeText(context, "Ingredient removed", Toast.LENGTH_SHORT).show();
    }

    private void updateIngredientsInRepo() {
        RecipeRepository repo = RecipeRepository.getInstance();
        repo.clearIngredientsForRecipe(recipeId);
        for (RecipeIngredient ing : ingredients) {
            repo.addIngredientToRecipe(recipeId, ing.getIngredientId(), ing.getQuantity());
        }
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text1;
        public TextView text2;

        public ViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}
