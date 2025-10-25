package com.example.utaste.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.utaste.R;

import java.util.List;

public class RecipeImageAdapter extends ArrayAdapter<RecipeImage> {

    public RecipeImageAdapter(Context context, List<RecipeImage> images) {
        super(context, 0, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return inflateRow(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return inflateRow(position, convertView, parent);
    }

    private View inflateRow(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.spinner_image_item, parent, false);
        }

        RecipeImage recipeImage = getItem(position);

        ImageView icon = convertView.findViewById(R.id.spinnerImageIcon);
        TextView label = convertView.findViewById(R.id.spinnerImageLabel);

        icon.setImageResource(recipeImage.getResId());
        label.setText(recipeImage.getName());

        return convertView;
    }
}

