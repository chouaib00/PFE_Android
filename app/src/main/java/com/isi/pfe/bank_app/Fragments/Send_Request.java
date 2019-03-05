package com.isi.pfe.bank_app.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.isi.pfe.bank_app.R;

public class Send_Request extends Fragment implements View.OnClickListener {
    Toolbar toolbar;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.drawer_item_send_request));
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.fragment_send_request, container, false);
        toolbar = (Toolbar) myInflatedView.findViewById(R.id.toolbarrequest);
        Button btn_card = (Button) myInflatedView.findViewById(R.id.btn_debit_card);
        btn_card.setOnClickListener(this);
        Button btn_checkbook = (Button) myInflatedView.findViewById(R.id.btn_checkbook);
        btn_checkbook.setOnClickListener(this);
        return myInflatedView;
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_debit_card){
            CreditCard_request nextFrag= new CreditCard_request();
            this.getFragmentManager().beginTransaction()
                    .replace(R.id.content_main, nextFrag,"request")
                    .addToBackStack(null)
                    .commit();
        }
        else if (v.getId() == R.id.btn_checkbook){
            Checkbook nextFrag= new Checkbook();
            this.getFragmentManager().beginTransaction()
                    .replace(R.id.content_main, nextFrag,"request")
                    .addToBackStack(null)
                    .commit();
        }


    }

}
