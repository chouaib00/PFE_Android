package com.isi.pfe.bank_app.Fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isi.pfe.bank_app.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class Credit_Simulator extends Fragment {
    Toolbar toolbar;
    MaterialEditText amount;
    DiscreteSeekBar seekBar;
    MaterialEditText month;
    DiscreteSeekBar seekBarMonth;
    Button btn;
    TextView amoutText;
    TextView monthText;
    TextView result;
    RelativeLayout rl;
    int bool;
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Credit Simulator");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        rl.animate().translationYBy(1000).start();
        bool = 1;
        seekBar.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                return value * 500;
            }
        });

        amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    try {
                        int value = Integer.parseInt(amount.getText().toString());
                        if ((value < 500) || (value > 100000))
                            amount.setError("");
                        else {
                            value = (int) Math.round(value/100.0) * 100;
                            value = value / 500;
                            seekBar.setProgress(value);
                        }
                    }catch (NumberFormatException e){
                        amount.setError("");
                    }
                }
            }
        });

        amount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    try {
                        int value = Integer.parseInt(amount.getText().toString());
                        if ((value < 500) || (value > 100000))
                            amount.setError("");
                        else {
                            value = (int) Math.round(value/100.0) * 100;
                            value = value / 500;
                            seekBar.setProgress(value);
                        }
                    }catch (NumberFormatException e){
                        amount.setError("");
                    }
                }
                return false;
            }
        });

        month.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    try {
                        int value = Integer.parseInt(month.getText().toString());
                        if ((value < 0) || (value > 240))
                            month.setError("");
                        else
                            seekBarMonth.setProgress(value);
                    }catch (NumberFormatException e){
                        month.setError("");
                    }
                }
            }
        });

        month.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                        try {
                            int value = Integer.parseInt(month.getText().toString());
                            if ((value < 0) || (value > 240))
                                month.setError("");
                            else
                                seekBarMonth.setProgress(value);
                        }catch (NumberFormatException e){
                            month.setError("");
                        }
                }
                return false;
            }
        });


        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                value = value * 500;
                value = (int) Math.round(value/100.0) * 100;
                amount.setText(value + "");
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        seekBarMonth.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                month.setText(value + "");
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bool !=1)
                    rl.animate().translationYBy(1000).start();
                bool = 0;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rl.animate().translationYBy(-1000).start();
                        amoutText.setText(amount.getText().toString()+" DT");
                        monthText.setText(month.getText().toString()+" months");
                        result.setText(Integer.toString(Math.round(Integer.parseInt(amount.getText().toString()) / Integer.parseInt(month.getText().toString()))));
                    }
                }, 500);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.fragment_credit__simulator, container, false);
        toolbar = (Toolbar) myInflatedView.findViewById(R.id.toolbarSimulator);
        amount = (MaterialEditText) myInflatedView.findViewById(R.id.sim_amount);
        seekBar = (DiscreteSeekBar) myInflatedView.findViewById(R.id.sim_slider_amount);
        month = (MaterialEditText) myInflatedView.findViewById(R.id.sim_months);
        seekBarMonth = (DiscreteSeekBar) myInflatedView.findViewById(R.id.sim_slider_months);
        btn = (Button) myInflatedView.findViewById(R.id.sim_btn);
        amoutText = (TextView) myInflatedView.findViewById(R.id.sim_amount_text);
        monthText = (TextView) myInflatedView.findViewById(R.id.sim_months_text);
        result = (TextView) myInflatedView.findViewById(R.id.sim_result);
        rl = (RelativeLayout) myInflatedView.findViewById(R.id.sim_result_layout);
        return myInflatedView;
    }

}
