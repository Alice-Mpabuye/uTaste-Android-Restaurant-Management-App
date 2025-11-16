package com.example.utaste.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.utaste.R;

import java.util.List;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder> {

        private List<String> localdata;
        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView textView;
            public ViewHolder(TextView v) {
                super(v);
                textView = v;
            }
            public TextView getTextView() {
                return textView;
            }
        }

        public TextAdapter(List<String> data) {
            localdata = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder((TextView) v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.getTextView().setText(localdata.get(position));
        }

        @Override
        public int getItemCount() {
            return localdata.size();
        }
}
