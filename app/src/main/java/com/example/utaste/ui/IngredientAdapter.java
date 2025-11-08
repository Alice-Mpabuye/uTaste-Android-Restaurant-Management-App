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

    // Click listener interfaces
    public interface OnItemClickListener {
        void onItemClick(int position, RecipeIngredient ingredient);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(int position, RecipeIngredient ingredient);
    }

    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    public IngredientAdapter(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
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
        holder.tvName.setText(ing.getName() != null ? ing.getName() : "No name");
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
