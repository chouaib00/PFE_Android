package com.isi.pfe.bank_app.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.isi.pfe.bank_app.Adapters.ClientInfo_Adapter;
import com.isi.pfe.bank_app.Classes.Account;
import com.isi.pfe.bank_app.Classes.Account_Management;
import com.isi.pfe.bank_app.Classes.Client_Management;
import com.isi.pfe.bank_app.Classes.Model;
import com.isi.pfe.bank_app.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
import java.util.List;


public class Client_Informations extends Fragment {

    RecyclerView rv;
    List<Model> listModel;
    int client_code;
    ClientInfo_Adapter adapter;
    Toolbar toolbar;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.Home));
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        client_code = Account_Management.client_code;
        System.out.println(client_code);
        listModel=new ArrayList();
        InitializeAdapter();
        if(Client_Management.usable)
            listModel.add(new Model(0, Client_Management.fName +" "+Client_Management.lName,Client_Management.email,Client_Management.address,Client_Management.phone,Client_Management.Banker));
        else
            Toast.makeText(getContext(), getString(R.string.error_reconnect), Toast.LENGTH_LONG).show();
        for(int i=0;i<Account_Management.listAccount.size();i++){
            Account a = Account_Management.listAccount.get(i);
            listModel.add(new Model(1, a.num,a.type,a.balance,a.banker,a.date,a.IBAN));
        }
        adapter.notifyDataSetChanged();
    }

    public void InitializeAdapter(){
        adapter = new ClientInfo_Adapter(listModel,getContext());
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL, false);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.setItemViewCacheSize(20);
        rv.setDrawingCacheEnabled(true);
        rv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_client_informations, container,false);
        toolbar = (Toolbar) myInflatedView.findViewById(R.id.toolbar);
        rv=(RecyclerView)myInflatedView.findViewById(R.id.rv);
        return myInflatedView;
    }

}
