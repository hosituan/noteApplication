package com.example.noteapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.noteapplication.R;
import com.example.noteapplication.bean.Status;
import com.example.noteapplication.database.MyDatabaseHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    AnyChartView anyChartView;
    MyDatabaseHelper db;
    public HomeFragment() {

    }
    public void setupPieChart() throws ParseException {
        Pie pie = AnyChart.pie();
        List<Status> statuses = db.getAllStatus();
        List<DataEntry> dataEntries = new ArrayList<>();

        for (int i=0; i < statuses.size(); i++) {
            String title = statuses.get(i).getTitle();
            int count = db.countStatus(title);
            dataEntries.add(new ValueDataEntry(title, count));
        }

        pie.data(dataEntries);
        pie.title("Dashboard");
        anyChartView.setChart(pie);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.db = new MyDatabaseHelper(getActivity());
        View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        anyChartView = fragmentView.findViewById(R.id.any_chart_view);
        try {
            setupPieChart();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return fragmentView;

    }
}
