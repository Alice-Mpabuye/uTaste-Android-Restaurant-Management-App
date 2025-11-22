package com.example.utaste.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.utaste.R;
import com.example.utaste.data.Sale;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple adapter: items are either HEADER (type 0) or SALE (type 1).
 * HEADER item: uses item_sales_summary_header.xml and holds two fields:
 *   - tvRecipeNameHeader
 *   - tvRecipeStats
 * SALE item: uses item_sale.xml and displays rating/date and note.
 *
 * To populate: call setData(...) with a precomputed flattened list where
 * header entries are encoded as Sale with id = -1 and recipeName filled, and next items are actual sales.
 */
public class SalesSummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_SALE = 1;

    private final List<Object> items = new ArrayList<>(); // mix of HeaderModel and Sale

    public static class HeaderModel {
        String recipeName;
        int count;
        double avgRating;
        HeaderModel(String recipeName, int count, double avgRating) {
            this.recipeName = recipeName;
            this.count = count;
            this.avgRating = avgRating;
        }
    }

    public void setData(List<Object> flattened) {
        items.clear();
        items.addAll(flattened);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object o = items.get(position);
        return (o instanceof HeaderModel) ? TYPE_HEADER : TYPE_SALE;
    }

    @Override
    public int getItemCount() { return items.size(); }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sales_summary_header, parent, false);
            return new HeaderHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale, parent, false);
            return new SaleHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        Object o = items.get(position);
        if (vh instanceof HeaderHolder) {
            HeaderModel h = (HeaderModel)o;
            ((HeaderHolder) vh).tvName.setText(h.recipeName);
            ((HeaderHolder) vh).tvStats.setText(h.count + " sales • Avg: " + String.format("%.2f", h.avgRating));
        } else {
            Sale s = (Sale) o;
            SaleHolder sh = (SaleHolder) vh;
            sh.tvNote.setText(s.getNote() == null ? "" : s.getNote());
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
            String stars = getStars(s.getRating());
            sh.tvMeta.setText(stars + " — " + df.format(s.getTimestamp()));
        }
    }

    private String getStars(int rating) {
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<rating;i++) sb.append("★");
        for (int i=rating;i<5;i++) sb.append("☆");
        return sb.toString();
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvStats;
        HeaderHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvRecipeNameHeader);
            tvStats = v.findViewById(R.id.tvRecipeStats);
        }
    }

    static class SaleHolder extends RecyclerView.ViewHolder {
        TextView tvMeta, tvNote;
        SaleHolder(View v) {
            super(v);
            tvMeta = v.findViewById(R.id.tvSaleMeta);
            tvNote = v.findViewById(R.id.tvSaleNote);
        }
    }
}
