package com.isi.pfe.bank_app.Fragments;


import android.app.DatePickerDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import com.isi.pfe.bank_app.Adapters.Transactions_Adapter;
import com.isi.pfe.bank_app.Classes.Account_Management;
import com.isi.pfe.bank_app.Classes.AdvancedSearch;
import com.isi.pfe.bank_app.R;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.truizlop.fabreveallayout.FABRevealLayout;
import com.truizlop.fabreveallayout.OnRevealChangeListener;

import static com.isi.pfe.bank_app.Classes.Account_Management.getAccountNumberString;


public class Transactions extends Fragment {
    final int NUMBER_ITEM = 7; //Load
    int limit = -NUMBER_ITEM;
    EditText dateImin,dateIMax;
    Button buttonSet;
    Transactions_Adapter listAdapter;
    ExpandableListView expListView;
    List<String[]> listDataHeader;
    HashMap<String[], String[]> listDataChild;
    FABRevealLayout fabRevealLayoutAdvSearch;
    ArrayList<String> listCategories;
    Spinner spinner;
    ArrayAdapter<String> spinnerAdapter;
    CheckedTextView receivedCheck,sentCheck;
    TextView reset,remove,account_number,balance;
    boolean noMoreData=false;
    SwipyRefreshLayout swipyRefreshLayout;
    FloatingActionButton FABReturn;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.tabs_transaction));
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_transactions, container,false);
        inflate(myInflatedView);
        return myInflatedView;
    }


    //TODO:Init
    private void inflate(View myInflatedView) {
        //Account card
        account_number = (TextView) myInflatedView.findViewById(R.id.accountCardRA_NumAccount);
        balance = (TextView) myInflatedView.findViewById(R.id.accountCardRA_Balance);
        //Advanced Search
        dateImin = (EditText) myInflatedView.findViewById(R.id.recentActivitiesAS_dateInputMin);
        dateIMax = (EditText) myInflatedView.findViewById(R.id.recentActivitiesAS_dateInputMax);
        spinner = (Spinner)myInflatedView.findViewById(R.id.recentActivitiesAS_Spinner);
        receivedCheck = (CheckedTextView) myInflatedView.findViewById(R.id.recentActivitiesAS_ReceivedCheck);
        sentCheck = (CheckedTextView) myInflatedView.findViewById(R.id.recentActivitiesAS_SentCheck);
        buttonSet = (Button) myInflatedView.findViewById(R.id.recentActivitiesAS_buttonSet);
        reset = (TextView) myInflatedView.findViewById(R.id.recentActivitiesAS_Reset);
        remove = (TextView) myInflatedView.findViewById(R.id.recentActivitiesAS_Remove);
        FABReturn = (FloatingActionButton) myInflatedView.findViewById(R.id.recentActivitiesAS_FABReturn);
        //Transactions
        expListView = (ExpandableListView) myInflatedView.findViewById(R.id.recentActivities_listView);
        //Float menu
        fabRevealLayoutAdvSearch = (FABRevealLayout) myInflatedView.findViewById(R.id.recentActivities_fabRevealLayoutAdvSearch);
        //Refresh
        swipyRefreshLayout = (SwipyRefreshLayout) myInflatedView.findViewById(R.id.swipyrefreshlayout);
    }
    private void init() {
        noMoreData=false;
        limit = -NUMBER_ITEM;
        //Account Card
        initCard();
        //Float menu
        configureFABReveal();
        //Transactions
        listDataHeader = new ArrayList<String[]>();
        listDataChild = new HashMap<String[], String[]>();
        initializeListAdapter();
        makeListListener();

        //Advanced Search
        new AdvancedSearch();
        dateIMax.setText(AdvancedSearch.getMaxDate());
        dateImin.setText(AdvancedSearch.getMinDate());
        makeDate();
        makeAdvancedSearchListeners();

        listCategories = new ArrayList<String>();
        getCategories();
        initializeSpinnerAdapter();


        InvokeTransactionService();
    }
    private void initializeListAdapter(){
        listAdapter = new Transactions_Adapter(getContext(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }
    private void initializeSpinnerAdapter(){
        spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, listCategories);
        spinner.setAdapter(spinnerAdapter);
    }
    private void initCard() {
        account_number.setText(Account_Management.getAccountNumberString(Account_Management.getAccountNumberString(getContext())));
        balance.setText(Account_Management.balance);
    }



    //TODO:Listeners
    private void makeListListener() {
        swipyRefreshLayout.setEnabled(true);
        swipyRefreshLayout.setDistanceToTriggerSync(10);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (!noMoreData)
                    InvokeTransactionService();
                else
                    setNoMoreData();
            }
        });
    }
    private void makeAdvancedSearchListeners() {
        sentCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvancedSearch.sent = !sentCheck.isChecked();
                sentCheck.setChecked(!sentCheck.isChecked());
            }
        });
        receivedCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sentCheck.isChecked())
                AdvancedSearch.received=!receivedCheck.isChecked();
                receivedCheck.setChecked(!receivedCheck.isChecked());
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AdvancedSearch();
                makeDate();
            }
        });
        FABReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabRevealLayoutAdvSearch.revealMainView();
            }
        });
    }
    private void makeDate() {
        final Calendar myCalendarMin = Calendar.getInstance();
        final Calendar myCalendarMax = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateMax;
        final DatePickerDialog.OnDateSetListener dateMin;

        dateMax = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendarMax.set(Calendar.YEAR, year);
                myCalendarMax.set(Calendar.MONTH, monthOfYear);
                myCalendarMax.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                AdvancedSearch.day_Max = dayOfMonth;
                AdvancedSearch.month_Max = monthOfYear;//+ 1) % 12;
                AdvancedSearch.year_Max = year;
                dateIMax.setText(AdvancedSearch.getMaxDate());

            }

        };

        dateMin = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendarMin.set(Calendar.YEAR, year);
                myCalendarMin.set(Calendar.MONTH, monthOfYear);
                myCalendarMin.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                AdvancedSearch.day_min = dayOfMonth;
                AdvancedSearch.month_min = monthOfYear;
                AdvancedSearch.year_min = year;
                dateImin.setText(AdvancedSearch.getMinDate());

            }

        };

        dateIMax.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myCalendarMax.set(AdvancedSearch.year_Max, AdvancedSearch.month_Max, AdvancedSearch.day_Max);
                new DatePickerDialog(getContext(), dateMax, myCalendarMax
                        .get(Calendar.YEAR), myCalendarMax.get(Calendar.MONTH),
                        myCalendarMax.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        dateImin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AdvancedSearch.initDateMin();
                myCalendarMin.set(AdvancedSearch.year_min, AdvancedSearch.month_min, AdvancedSearch.day_min);
                new DatePickerDialog(getContext(), dateMin, myCalendarMin
                        .get(Calendar.YEAR), myCalendarMin.get(Calendar.MONTH),
                        myCalendarMin.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void makeSetButtonListener() {
        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvancedSearch.category = spinner.getSelectedItem().toString();
                listDataHeader = new ArrayList<String[]>();
                listDataChild = new HashMap<String[], String[]>();
                initializeListAdapter();
                swipyRefreshLayout.setOnRefreshListener(null);
                swipyRefreshLayout.setEnabled(false);
                InvokeTransactionServiceAdvSearch();
                remove.setVisibility(View.VISIBLE);
                makeRemoveListener();
                fabRevealLayoutAdvSearch.revealMainView();
            }
        });
    }
    private void makeRemoveListener() {
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDataHeader = new ArrayList<>();
                listDataChild = new HashMap<>();
                initializeListAdapter();
                limit = -NUMBER_ITEM;
                InvokeTransactionService();
                makeListListener();
                remove.setVisibility(View.INVISIBLE);
                remove.setOnClickListener(null);
            }
        });
    }


    //TODO:Services
    public void InvokeTransactionService() {
        noMoreData=false;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String accountNumber= getAccountNumberString(getContext());
        if (accountNumber.length() > 0) {
            params.put("account_number", getAccountNumberString(getContext()));
            params.put("limit", getLimit());
            client.get(getString(R.string.serverIP) + "/WebServices/transactions/getTransactionsLimit", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("status")) {
                            int count = obj.getInt("count");
                            if (count > 0) {
                                for (int i = 0; i < count; i++) {
                                    JSONObject o = obj.getJSONObject("Transaction_" + i);
                                    if ((o.getLong("Sender") != Account_Management.Chosen_Account) && (receivedCheck.isChecked())) {
                                        listDataHeader.add(new String[]{"1", o.getString("Category"), o.getString("Amount"), o.getString("Date").substring(0, o.getString("Date").length() - 5)});
                                        listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), new String[]{getAccountNumberString(o.getString("Sender")), o.getString("Old_Balance_R"), String.valueOf(o.getDouble("Old_Balance_R") + o.getDouble("Amount"))});
                                    } else if ((o.getLong("Sender") == Account_Management.Chosen_Account) && (sentCheck.isChecked())) {
                                        listDataHeader.add(new String[]{"2", o.getString("Category"), o.getString("Amount"), o.getString("Date").substring(0, o.getString("Date").length() - 5)});
                                        listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), new String[]{getAccountNumberString(o.getString("Receiver")), o.getString("Old_Balance_S"), String.valueOf(o.getDouble("Old_Balance_S") - o.getDouble("Amount"))});

                                    }
                                }
                                listAdapter.notifyDataSetChanged();
                                expListView.post(new Runnable() {
                                    @Override
                                    public void run() {int index = expListView.getCount() > NUMBER_ITEM ? (expListView.getCount() - (expListView.getCount() % NUMBER_ITEM)) : 0;
                                        expListView.setSelection(index);
                                        swipyRefreshLayout.setRefreshing(false);
                                    }
                                });
                                if (count < NUMBER_ITEM) noMoreData = true;
                            } else
                                setNoMoreData();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.error_falseAccount), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), getString(R.string.error_JSON), Toast.LENGTH_LONG).show();

                    }
                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    if (statusCode == 404) {
                        Toast.makeText(getContext(), getString(R.string.error_404), Toast.LENGTH_LONG).show();
                        InvokeTransactionService();
                    }
                    // When Http response code is '500'
                    else if (statusCode == 500) {
                        Toast.makeText(getContext(), getString(R.string.error_500), Toast.LENGTH_LONG).show();
                        InvokeTransactionService();
                    }
                    // When Http response code other than 404, 500
                    else {
                        Toast.makeText(getContext(), getString(R.string.error_unexpected), Toast.LENGTH_LONG).show();
                        InvokeTransactionService();
                    }
                }
            });
        }
    }
    public void InvokeTransactionServiceAdvSearch() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("account_number",String.valueOf(Account_Management.Chosen_Account));
        params.put("date_min", AdvancedSearch.getMinDate());
        params.put("date_max", AdvancedSearch.getMaxDate());
        params.put("category", AdvancedSearch.category);
        params.put("type", AdvancedSearch.getType());
        System.err.println("min: "+AdvancedSearch.getMinDate()+" max: "+AdvancedSearch.getMaxDate());
        client.get(getString(R.string.serverIP)+"/WebServices/transactions/getTransactionsDate",params ,new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getBoolean("status")){
                        int count = obj.getInt("count");
                        for(int i=0;i<count;i++) {
                            JSONObject o = obj.getJSONObject("Transaction_" + i);
                            if ((o.getLong("Sender") != Account_Management.Chosen_Account) && (receivedCheck.isChecked())){
                                listDataHeader.add(new String[]{"1", o.getString("Category"), o.getString("Amount"), o.getString("Date").substring(0, o.getString("Date").length() - 5)});
                                listDataChild.put(listDataHeader.get(listDataHeader.size()-1), new String[]{getAccountNumberString(o.getString("Sender")), o.getString("Old_Balance_R"), String.valueOf(o.getDouble("Old_Balance_R") + o.getDouble("Amount"))});
                            } else if ((o.getLong("Sender") == Account_Management.Chosen_Account) && (sentCheck.isChecked())) {
                                listDataHeader.add(new String[]{"2", o.getString("Category"), o.getString("Amount"), o.getString("Date").substring(0, o.getString("Date").length() - 5)});
                                listDataChild.put(listDataHeader.get(listDataHeader.size()-1), new String[]{getAccountNumberString(o.getString("Receiver")), o.getString("Old_Balance_S"), String.valueOf(o.getDouble("Old_Balance_S") - o.getDouble("Amount"))});
                            }
                        }
                        if(listDataHeader.isEmpty()) {
                            listDataHeader.add(new String[]{"3", "No data found"});
                        }
                        listAdapter.notifyDataSetChanged();

                    }
                    else{
                        Toast.makeText(getContext(), getString(R.string.error_falseAccount), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), getString(R.string.error_JSON), Toast.LENGTH_LONG).show();

                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,String content) {
                if(statusCode == 404){
                    Toast.makeText(getContext(), getString(R.string.error_404), Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getContext(), getString(R.string.error_500), Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getContext(), getString(R.string.error_unexpected), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void getCategories() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getString(R.string.serverIP)+"/WebServices/transactions/getTransactionsCategories" ,new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    listCategories.add("ALL");
                    if(obj.getBoolean("status")){
                        int count = obj.getInt("count");
                        for(int i=0;i<count;i++) {
                            listCategories.add(obj.getString("category_" + i));
                        }
                        spinnerAdapter.notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(getContext(), getString(R.string.error_falseCategory), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), getString(R.string.error_JSON), Toast.LENGTH_LONG).show();

                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,String content) {
                if(statusCode == 404){
                    Toast.makeText(getContext(), getString(R.string.error_404), Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getContext(), getString(R.string.error_500), Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getContext(), getString(R.string.error_unexpected), Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    //TODO:functions
    private String getLimit() {
        limit += NUMBER_ITEM;
        return limit+","+NUMBER_ITEM;
    }
    private void setNoMoreData() {
        if (!noMoreData) noMoreData = true;
        Toast.makeText(getContext(), getString(R.string.transaction_noMoreDate), Toast.LENGTH_SHORT).show();
        swipyRefreshLayout.setRefreshing(false);
    }

    //TODO:FloatMenu
    private void configureFABReveal() {
        fabRevealLayoutAdvSearch.setOnRevealChangeListener(new OnRevealChangeListener() {
            @Override
            public void onMainViewAppeared(FABRevealLayout fabRevealLayout, View mainView) {
                showMainViewItems();
            }

            @Override
            public void onSecondaryViewAppeared(final FABRevealLayout fabRevealLayout, View secondaryView) {
                showSecondaryViewItems();
                prepareBackTransition(fabRevealLayout);
            }
        });
    }
    private void showMainViewItems() {
        scale(expListView, 500);
    }
    private void showSecondaryViewItems() {
        scale(dateImin, 600);
        scale(dateIMax, 900);
        scale(buttonSet, 1500);
    }
    private void prepareBackTransition(final FABRevealLayout fabRevealLayout) {
        makeSetButtonListener();
    }
    private void scale(View view, long delay){
        view.setScaleX(0);
        view.setScaleY(0);
        view.animate()
                .scaleX(1)
                .scaleY(1)
                .setDuration(500)
                .setStartDelay(delay)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }
}