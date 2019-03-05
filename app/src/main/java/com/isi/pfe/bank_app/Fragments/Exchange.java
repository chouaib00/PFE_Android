package com.isi.pfe.bank_app.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.isi.pfe.bank_app.Classes.BaseItem;
import com.isi.pfe.bank_app.Classes.ConstructCurrency;
import com.isi.pfe.bank_app.Classes.CurrencyItem;
import com.isi.pfe.bank_app.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.FooterAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter_extensions.items.ProgressItem;
import com.mikepenz.iconics.view.IconicsButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Exchange extends Fragment implements View.OnClickListener {
    public static final String ACCESS_KEY = "bc6271863f144196982ecc0c2429e4d9";
    public static final String BASE_URL = "https://openexchangerates.org/api/";
    public static final String ENDPOINT = "latest.json";
    private RequestParams requestParams;
    List<String> currencyList;
    JSONObject result;
    private RecyclerView rv;
    FastItemAdapter fastAdapter;
    IconicsButton b;
    MaterialDialog dialog;
    TextView convertttxt;
    ImageView flag;
    String chosenBase;
    JSONObject flagIDs;
    JSONObject symbDesc;
    float basechange=0;
    List<Float> changeList = new ArrayList<>();
    Toolbar toolbar;
    DecimalFormat df;
    ArrayList<String> listtags;
    FooterAdapter<ProgressItem> footerAdapter;
    int currentmax;

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        requestParams = new RequestParams();
        requestParams.add("app_id",ACCESS_KEY);
        requestParams.add("base","USD");
        getActivity().setTitle("Exchange rates");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        chosenBase = "USD";
        df = new DecimalFormat("#.########");
        constructList();
        ConstructCurrency construct = new ConstructCurrency();
        flagIDs = construct.ConstructFlags();
        symbDesc = construct.ConstructSymboleDescription();
        JSONArray listTags = flagIDs.names();
        listtags = new ArrayList<>();
        JSONArray jsonArray = listTags;
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i=0;i<len;i++){
                try {
                    listtags.add(listTags.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Collections.sort(listtags);

        fastAdapter = new FastItemAdapter();
        invokeService();
        LinearLayoutManager mlinearlayout = new LinearLayoutManager(getContext());
        rv.setLayoutManager(mlinearlayout);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(fastAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(rv.getContext(),
                mlinearlayout.getOrientation());
        rv.addItemDecoration(mDividerItemDecoration);
        final FastItemAdapter dialgoAdapter = new FastItemAdapter();
        List<BaseItem> items = new ArrayList<>();

        BaseItem item;
        for(int i=0;i<listtags.size();i++){
            try {
                item = new BaseItem()
                        .withFlag(ContextCompat.getDrawable(getContext(),flagIDs.getInt(listtags.get(i))))
                        .withFlagDesc(getResources().getString(symbDesc.getInt(listtags.get(i))))
                        .withID(listtags.get(i)).withContext(getContext())
                        .withFlagId(flagIDs.getInt(listtags.get(i)));
                items.add(item);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        currentmax = 20;
        footerAdapter = new FooterAdapter<>();


        dialgoAdapter.withOnClickListener(new FastAdapter.OnClickListener<BaseItem>() {
            @Override
            public boolean onClick(View v, IAdapter<BaseItem> adapter, BaseItem item, int position) {
                chosenBase = item.id;
                constructList();
                fastAdapter.clear();
                changeList.clear();
                try {
                    basechange = 1/Float.parseFloat(result.getString(item.id));
                    for(int i=0;i<currencyList.size();i++){
                        float curency =1/Float.parseFloat(result.getString(currencyList.get(i)));
                        CurrencyItem c = new CurrencyItem().withCurrency_symbole(ContextCompat.getDrawable(getContext(), R.drawable.dollarsymbol))
                                .withCurrency_change(df.format(basechange/curency))
                                .withCurrency_change_desc("1 "+chosenBase+" = "+df.format(basechange/curency)+" "+currencyList.get(i))
                                .withCurrency_symbole_desc(getResources().getString(symbDesc.getInt(currencyList.get(i))))
                                .withCountry_image(ContextCompat.getDrawable(getContext(),flagIDs.getInt(currencyList.get(i))));
                        changeList.add(basechange/curency);
                        fastAdapter.add(c);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                fastAdapter.notifyAdapterDataSetChanged();
                flag.setImageDrawable(item.flag);
                convertttxt.setText("1");
                dialog.hide();
                return false;
            }
        });
        dialgoAdapter.add(items);
        dialog = new MaterialDialog.Builder(getContext())
                .title(R.string.dialog_title)
                .adapter(dialgoAdapter,null)
                .titleColorRes(R.color.material_orange_500)
                .positiveColorRes(R.color.material_orange_500)
                .neutralColorRes(R.color.material_orange_500)
                .dividerColor(getResources().getColor(R.color.material_orange_500))
                .build();

    }

    private void initCurrency(){
        currencyList = new ArrayList<>();
        currencyList.add("USD");
        currencyList.add("EUR");
        currencyList.add("GBP");
        currencyList.add("TND");
        currencyList.add("CAD");
        currencyList.add("JPY");
    }

    private void constructList(){
        initCurrency();
        for (int i=0;i<currencyList.size();i++){
            if (currencyList.get(i) ==chosenBase)
                currencyList.remove(i);
        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.currency_base)
            dialog.show();
        else if(v.getId() == R.id.convertButton){
            float value;
            if(convertttxt.length()==0) {
                value = 1;
                convertttxt.setText("1");
            }
            else
                value = Float.parseFloat(convertttxt.getText().toString());
            for(int i=0;i<fastAdapter.getAdapterItemCount();i++){
                CurrencyItem item = (CurrencyItem) fastAdapter.getAdapterItem(i);
                item.currency_change = df.format(changeList.get(i) * value);
                fastAdapter.set(i,item);
            }
            fastAdapter.notifyAdapterDataSetChanged();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflater = inflater.inflate(R.layout.fragment_exchange, container, false);
        rv = (RecyclerView) myInflater.findViewById(R.id.rvExchange);
        b = (IconicsButton) myInflater.findViewById(R.id.convertButton);
        b.setOnClickListener(this);
        convertttxt = (TextView) myInflater.findViewById(R.id.edittextexchange);
        flag = (ImageView) myInflater.findViewById(R.id.currency_base);
        flag.setOnClickListener(this);
        toolbar = (Toolbar) myInflater.findViewById(R.id.toolbarexchange);
        return myInflater;
    }
    public void invokeService() {
        AsyncHttpClient request = new AsyncHttpClient();
        request.get(BASE_URL+ENDPOINT,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    List<CurrencyItem> items = new ArrayList<>();
                    result = response.getJSONObject("rates");
                    for(int i=0;i<currencyList.size();i++){
                        CurrencyItem item = new CurrencyItem().withCurrency_symbole(ContextCompat.getDrawable(getContext(), R.drawable.dollarsymbol))
                                .withCurrency_change(response.getJSONObject("rates").getString(currencyList.get(i)))
                                .withCurrency_change_desc("1 "+chosenBase+" = "+response.getJSONObject("rates").getString(currencyList.get(i))+" "+currencyList.get(i))
                                .withCurrency_symbole_desc(getResources().getString(symbDesc.getInt(currencyList.get(i))))
                                .withCountry_image(ContextCompat.getDrawable(getContext(),flagIDs.getInt(currencyList.get(i))));
                        changeList.add(Float.parseFloat(response.getJSONObject("rates").getString(currencyList.get(i))));
                        items.add(item);
                    }
                    fastAdapter.add(items);
                    fastAdapter.notifyAdapterDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
