package com.isi.pfe.bank_app.Fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.isi.pfe.bank_app.Classes.stats;
import com.isi.pfe.bank_app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class my_stats extends Fragment {
    Toolbar toolbar;

    public static int NUMBER_MONTHS = 7; //To Show
    public static String[] axeX;

    Calendar calendar;
    String[][] months;

    private LineChartView chartTop;
    private ColumnChartView chartBottom;
    private LineChartData lineData;

    TextView accountNumber,NetValue,currShow;
    Spinner spinner;
    Button set;
    FloatingActionButton floatMenu;
    Dialog dialog;
    public static boolean usable=false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My stats");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        useInit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_my_stats, container,false);
        toolbar = (Toolbar) myInflatedView.findViewById(R.id.toolbarstats);
        new stats(getContext(),getString(R.string.serverIP));
        inflate(myInflatedView);
        return myInflatedView;
    }

    //TODO:Init
    public void useInit(){
        axeX = getResources().getStringArray(R.array.weekArray);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int retry =0;
                while ((!usable) &&(retry <5)) {
                   // stats.refreshData();
                    retry++;
                }
                if(usable)
                    init();
                else {
                    Toast.makeText(getContext(), getString(R.string.error_falseStats), Toast.LENGTH_LONG).show();
                    stats.hide();
                    stats.refreshData();
                    useInit();
                }
            }
        }, 1000);
    }
    public void init() {
        calendar = Calendar.getInstance();
        months = new String[NUMBER_MONTHS][2];
        String currMonth="";
        calendar.add(Calendar.MONTH,-NUMBER_MONTHS+1);
        for(int i=0;i<NUMBER_MONTHS;i++){
            //TODO:LANG
            currMonth = new SimpleDateFormat("MMM", Locale.ENGLISH).format(calendar.getTime());
            months[i][1] =  String.valueOf(calendar.get(Calendar.YEAR));
            months[i][0] = currMonth;
            calendar.add(Calendar.MONTH,+1);
        }
        generateInitialLineData();
        generateColumnData();

        String strFormat = getResources().getString(R.string.myStat_CurrShow);
        String strMsg = String.format(strFormat, NUMBER_MONTHS);
        currShow.setText(strMsg);
        makeShowListener();
     }
    public void inflate(View myInflatedView) {
        chartTop = (LineChartView) myInflatedView.findViewById(R.id.chart_top);
        chartBottom = (ColumnChartView) myInflatedView.findViewById(R.id.chart_bottom);
        accountNumber = (TextView) myInflatedView.findViewById(R.id.myStat_accountNumber);
        NetValue = (TextView) myInflatedView.findViewById(R.id.myStat_accountNet);
        currShow = (TextView) myInflatedView.findViewById(R.id.myStat_currShow);
        floatMenu = (FloatingActionButton) myInflatedView.findViewById(R.id.myStat_FloatMenu);
    }


    //TODO:Listeners
    public void makeShowListener(){
        floatMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.fragment_mystats_dialog);
                dialog.show();

                spinner = (Spinner) dialog.findViewById(R.id.myStatDialog_Spinner);
                set = (Button) dialog.findViewById(R.id.myStatDialog_set);
                spinner.setSelection(NUMBER_MONTHS-1);
                makeSetListener();
            }
        });
    }
    public void makeSetListener(){
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected = Integer.parseInt(spinner.getSelectedItem().toString());
                if( NUMBER_MONTHS != selected) {
                   NUMBER_MONTHS = selected;
                   stats.refreshData();
                   useInit();
                }
                dialog.hide();
            }
        });
    }
    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            generateLineData(columnIndex,1);
        }

        @Override
        public void onValueDeselected() {

            generateLineData(0, 0);

        }
    }

    //TODO:GenerateData
    private void generateColumnData() {
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        float max=0,min=0;
        for (int i = 0; i < months.length; ++i) {
            values = new ArrayList<SubcolumnValue>();
            float value = stats.getMonthStat(i);
            int color;
            max = value>max ? value:max;
            min = value<min ? value:min;
            if((value <= 50) && (value >= -50))
                color = ChartUtils.COLOR_ORANGE;
            else if (value < 50)
                color = ChartUtils.COLOR_RED;
            else
                color = ChartUtils.COLOR_GREEN;
           // ChartUtils.pickColor()
            values.add(new SubcolumnValue(value,color));

            axisValues.add(new AxisValue(i).setLabel(months[i][0]));

            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
            NetValue.setText(String.valueOf(stats.getMonthStat(months.length-1)));

        }

        ColumnChartData columnData = new ColumnChartData(columns);
        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(false).setName(getString(R.string.months)).setTextColor(ChartUtils.COLOR_VIOLET).setHasSeparationLine(true).setHasTiltedLabels(true));
        columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(5).setHasSeparationLine(true).setTextColor(ChartUtils.COLOR_VIOLET));
        chartBottom.setColumnChartData(columnData);
        chartBottom.setOnValueTouchListener(new ValueTouchListener());
        chartBottom.setValueSelectionEnabled(true);
        chartBottom.setZoomType(ZoomType.HORIZONTAL);
        Viewport v = new Viewport((float)-0.7, (int)max+10, (float) (NUMBER_MONTHS-0.3), (int)min-10);
        chartBottom.setMaximumViewport(v);
        chartBottom.setCurrentViewport(v);

    }
    private void generateInitialLineData() {
        int numValues = 4;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, 0));
            axisValues.add(new AxisValue(i).setLabel(axeX[i]));
        }

        Line line_in = new Line(values);
        line_in.setColor(ChartUtils.COLOR_GREEN).setCubic(true);
        Line line_out = new Line(values);
        line_out.setColor(ChartUtils.COLOR_RED).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line_in);
        lines.add(line_out);

        lineData = new LineChartData(lines);
        lineData.setAxisXBottom(new Axis(axisValues).setHasLines(false).setHasSeparationLine(true).setName(getString(R.string.weeks)).setTextColor(ChartUtils.COLOR_VIOLET).setHasTiltedLabels(true));
        lineData.setAxisYLeft(new Axis().setHasLines(true).setTextColor(ChartUtils.COLOR_VIOLET));
        chartTop.setLineChartData(lineData);

        // For build-up animation you have to disable viewport recalculation.
        chartTop.setViewportCalculationEnabled(false);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport((float)-0.2, 0, (float) 3.2, 0);
        chartTop.setMaximumViewport(v);
        chartTop.setCurrentViewport(v);

        chartTop.setZoomType(ZoomType.HORIZONTAL);
    }
    private void generateLineData(int columnIndex,int range) {
        if(range != 0) {
            // Cancel last animation if not finished.
            int i;
            float max = 0, currPoint;
            chartTop.cancelDataAnimation();
            List<PointValue> values = new ArrayList<PointValue>();
            // Modify data targets
            Line line_in = lineData.getLines().get(0);// For this example there is always only one line.
            //  line.setColor(color);
            i = 0;
            for (PointValue value : line_in.getValues()) {
                currPoint = stats.getWeekStat_In(columnIndex, i);
                max = (currPoint > max) ? currPoint : max;
                // Change target only for Y value.
              //  value.setTarget(value.getX(), range * currPoint);
                i++;
                values.add(new PointValue(value.getX(),currPoint));
            }
            line_in.setValues(values);


            Line line_out = lineData.getLines().get(1);
            i = 0;
            values = new ArrayList<PointValue>();
            for (PointValue value : line_out.getValues()) {
                currPoint = stats.getWeekStat_Out(columnIndex, i);
                max = (currPoint > max) ? currPoint : max;
                // Change target only for Y value.
                value.setTarget(value.getX(), range * currPoint);
                i++;
                values.add(new PointValue(value.getX(),currPoint));
            }
            line_out.setValues(values);
            List<Line> lines = new ArrayList<Line>();
            lines.add(line_in);
            lines.add(line_out);
            lineData.setLines(lines);
           // chartTop.setLineChartData(lineData);
            lineData.getAxisYLeft().setMaxLabelChars(String.valueOf(max).length());
            Viewport v = new Viewport((float)-0.2, (int) max+10, (float) 3.2,0);
            chartTop.setMaximumViewport(v);
            chartTop.setCurrentViewport(v);
            // Start new data animation with 300ms duration;
            chartTop.startDataAnimation(300);
        }else {
            lineData.getAxisYLeft().setMaxLabelChars(0);
            Viewport v = new Viewport((float)-0.2, 0, (float) 3.2, 0);
            chartTop.setMaximumViewport(v);
            chartTop.setCurrentViewport(v);
        }

    }



}
