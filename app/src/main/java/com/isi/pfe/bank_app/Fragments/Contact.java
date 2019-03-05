package com.isi.pfe.bank_app.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.isi.pfe.bank_app.Classes.Client_Management;
import com.isi.pfe.bank_app.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Contact extends Fragment {
Toolbar toolbar;
    MaterialEditText first_name,last_name,email,message;
    AppCompatButton send;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.drawer_item_contact_us));
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        init();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_contact, container, false);
        inflate(myInflatedView);
        return myInflatedView;
    }

    //TODO:Init
    public void init() {
        first_name.setIconLeft(new IconicsDrawable(getContext())
                .icon(GoogleMaterial.Icon.gmd_account_box)
                .sizeDp(48));
        last_name.setIconLeft(new IconicsDrawable(getContext())
                .icon(GoogleMaterial.Icon.gmd_account_box_o)
                .sizeDp(48));
        email.setIconLeft(new IconicsDrawable(getContext())
                .icon(GoogleMaterial.Icon.gmd_email)
                .sizeDp(48));
        message.setIconLeft(new IconicsDrawable(getContext())
                .icon(GoogleMaterial.Icon.gmd_comment)
                .sizeDp(48));
        makeSendListener();
    }
    public void inflate(View myInflatedView){
        toolbar = (Toolbar) myInflatedView.findViewById(R.id.toolbar_contact);
        first_name = (MaterialEditText) myInflatedView.findViewById(R.id.contact_first_name);
        last_name = (MaterialEditText) myInflatedView.findViewById(R.id.contact_last_name);
        email = (MaterialEditText) myInflatedView.findViewById(R.id.contact_email);
        message = (MaterialEditText) myInflatedView.findViewById(R.id.contact_message);
        send = (AppCompatButton) myInflatedView.findViewById(R.id.contact_button);
    }

    //TODO:Listeners
    public void makeSendListener() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:check
                //check();
                invokeContactService();
            }
        });
    }

    //TODO:Services
    public void invokeContactService() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("client_code", Client_Management.code_client);
        params.put("email", email.getText().toString());
        params.put("name", first_name.getText().toString() + " " + last_name.getText().toString());
        params.put("country", "Tunisia"); //TODO:country static
        params.put("message", message.getText().toString());
        client.get(getString(R.string.serverIP)+"/WebServices/contact/createSupportTicket",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("status")) {
                        Toast.makeText(getContext(),"You will receive an email for support ticket [#" +obj.getString("ticket_code")+"]", Toast.LENGTH_LONG).show();

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
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getContext(), getString(R.string.error_500), Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getContext(), getString(R.string.error_unexpected), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
