package com.isi.pfe.bank_app.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isi.pfe.bank_app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DebitCardDescription extends Fragment {
    Toolbar toolbar;
    TextView Title;
    TextView desc;
    TextView subTitle;
    TextView list;


    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle b = getArguments();
        getActivity().setTitle(b.getString("ftitle"));
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        Title.setText(b.getString("title"));
        desc.setText(b.getString("desc"));
        subTitle.setText(b.getString("subTitle"));
        list.setText(b.getString("list"));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.fragment_debit_card_description, container, false);
        toolbar = (Toolbar) myInflatedView.findViewById(R.id.toolbarDescription);
        Title = (TextView) myInflatedView.findViewById(R.id.debit_card_Title);
        desc = (TextView) myInflatedView.findViewById(R.id.debit_card_desc);
        subTitle = (TextView) myInflatedView.findViewById(R.id.debit_card_subTitle);
        list = (TextView) myInflatedView.findViewById(R.id.debit_card_list);
        return myInflatedView;
    }

}
