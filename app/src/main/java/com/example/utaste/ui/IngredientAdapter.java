package com.example.utaste.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.utaste.data.RecipeIngredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private List<RecipeIngredient> ingredients;

    public IngredientAdapter(List<RecipeIngredient> ingredients, Object o) {
        this.ingredients = ingredients;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecipeIngredient ing = ingredients.get(position);
        if (ing.getName() != null) {
            holder.tvName.setText(ing.getName());
        } else {
            holder.tvName.setText("No name"); // Fallback text
        }
        holder.tvQty.setText(ing.getQuantity() + " %");
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvQty;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(android.R.id.text1);
            tvQty = itemView.findViewById(android.R.id.text2);
        }
    }

    public void addIngredient(RecipeIngredient ing){
        ingredients.add(ing);
        notifyItemInserted(ingredients.size() - 1);
    }
}
