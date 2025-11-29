package com.example.utaste.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utaste.R;
import com.example.utaste.data.Sale;
import com.example.utaste.data.SaleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SalesSummaryActivity extends AppCompatActivity {

    private RecyclerView rvSummary;
    private SalesSummaryAdapter adapter;
    private SaleRepository saleRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_sales_summary);

        rvSummary = findViewById(R.id.rvSalesSummary);
        rvSummary.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SalesSummaryAdapter();
        rvSummary.setAdapter(adapter);

        saleRepository = SaleRepository.getInstance();

        Button btnBack = findViewById(R.id.btnBackToWaiter);

        btnBack.setOnClickListener(v -> {
            Intent i = new Intent(SalesSummaryActivity.this, WaiterActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSummary();
    }

    private void loadSummary() {
        Map<Integer, List<Sale>> grouped = saleRepository.listSalesGroupedByRecipe();
        List<Object> flattened = new ArrayList<>();

        for (Map.Entry<Integer, List<Sale>> e : grouped.entrySet()) {
            List<Sale> sales = e.getValue();
            if (sales.isEmpty()) continue;
            String recipeName = sales.get(0).getRecipeName() == null ? "Unknown recipe" : sales.get(0).getRecipeName();
            int count = sales.size();
            double avg = saleRepository.getAverageRatingForRecipe(e.getKey());
            // header
            flattened.add(new SalesSummaryAdapter.HeaderModel(recipeName, count, avg));
            // then sales
            flattened.addAll(sales);
        }

        adapter.setData(flattened);
    }
}
