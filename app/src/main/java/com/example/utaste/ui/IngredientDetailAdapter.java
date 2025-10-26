package com.example.utaste.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utaste.R;
import com.example.utaste.data.RecipeIngredient;

import java.util.List;

public class IngredientDetailAdapter extends RecyclerView.Adapter<IngredientDetailAdapter.ViewHolder> {

    private final List<RecipeIngredient> ingredients;

    public IngredientDetailAdapter(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeIngredient ingredient = ingredients.get(position);
        holder.text1.setText(ingredient.getName());
        holder.text2.setText(String.format("%.0f%%", ingredient.getQuantity()));
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
