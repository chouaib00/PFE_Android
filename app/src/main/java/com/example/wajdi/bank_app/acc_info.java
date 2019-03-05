package com.example.wajdi.bank_app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by wajdi on 04/02/17.
 */

public class acc_info extends Fragment {
    TextView tName;
    TextView tEmail;
    TextView tSolde;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Account Info");
        client c = server.getClient(account_management.code_client);
        account a = server.getAccount(account_management.chosenAcc);
        tName.setText(c.getNom() + " " + c.getPrenom());
        tEmail.setText(c.getEmail());
        tSolde.setText(String.valueOf(a.getSolde()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.acc_info, container,false);
        tName = (TextView) myInflatedView.findViewById(R.id.nameInfo);
        tEmail = (TextView) myInflatedView.findViewById(R.id.emailInfo);
        tSolde = (TextView) myInflatedView.findViewById(R.id.soldeInfo);
        return myInflatedView;
    }
}
