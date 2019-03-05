package com.isi.pfe.bank_app.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isi.pfe.bank_app.Classes.AccountInfoItem;
import com.isi.pfe.bank_app.Classes.Account_Management;
import com.isi.pfe.bank_app.Classes.Client_Management;
import com.isi.pfe.bank_app.R;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
import java.util.List;


public class Account_Info extends Fragment {

    RecyclerView rv;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)  {
        super.onViewCreated(view,savedInstanceState);
        FastItemAdapter<AccountInfoItem> adapter = new FastItemAdapter<>();
        List<AccountInfoItem> items = new ArrayList<>();
        AccountInfoItem item;
        item = new AccountInfoItem().withIcon(new IconicsDrawable(getContext())
                .icon(GoogleMaterial.Icon.gmd_account)
                .color(getResources().getColor(R.color.material_orange_500))
                .sizeDp(48)).withTitle(getString(R.string.accountInfo_holder)).withsubTitle(Account_Management.name)
                .withColor(getResources().getColor(R.color.material_light_black));
        items.add(item);
        item = new AccountInfoItem().withIcon(new IconicsDrawable(getContext())
                .icon(FontAwesome.Icon.faw_credit_card)
                .color(getResources().getColor(R.color.material_orange_500))
                .sizeDp(48)).withTitle(getString(R.string.number)).withsubTitle(Account_Management.getAccountNumberString(Account_Management.getAccountNumberString(getContext())))
                .withColor(getResources().getColor(R.color.material_light_black));
        items.add(item);
        item = new AccountInfoItem().withIcon(new IconicsDrawable(getContext())
                .icon(FontAwesome.Icon.faw_usd)
                .color(getResources().getColor(R.color.material_orange_500))
                .sizeDp(48)).withTitle(getString(R.string.balance)).withsubTitle(Account_Management.balance)
                .withColor(getResources().getColor(R.color.material_green_500));
        items.add(item);
        item = new AccountInfoItem().withIcon(new IconicsDrawable(getContext())
                .icon(GoogleMaterial.Icon.gmd_album)
                .color(getResources().getColor(R.color.material_orange_500))
                .sizeDp(48)).withTitle(getString(R.string.accountInfo_type)).withsubTitle(Account_Management.type)
                .withColor(getResources().getColor(R.color.material_light_black));
        items.add(item);
        item = new AccountInfoItem().withIcon(new IconicsDrawable(getContext())
                .icon(FontAwesome.Icon.faw_university)
                .color(getResources().getColor(R.color.material_orange_500))
                .sizeDp(48)).withTitle(getString(R.string.accountInfo_banker)).withsubTitle(Account_Management.banker)
                .withColor(getResources().getColor(R.color.material_light_black));
        items.add(item);
        item = new AccountInfoItem().withIcon(new IconicsDrawable(getContext())
                .icon(FontAwesome.Icon.faw_cc_mastercard)
                .color(getResources().getColor(R.color.material_orange_500))
                .sizeDp(48)).withTitle(getString(R.string.transferMoney_IBANAddress)).withsubTitle(Account_Management.IBAN)
                .withColor(getResources().getColor(R.color.material_light_black));
        items.add(item);
        adapter.add(items);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(rv.getContext(),
                new LinearLayoutManager(getContext()).getOrientation());
        rv.addItemDecoration(mDividerItemDecoration);
        rv.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_account__info, container,false);
        rv = (RecyclerView) myInflatedView.findViewById(R.id.rv_account_info);
        return myInflatedView;
    }

}
