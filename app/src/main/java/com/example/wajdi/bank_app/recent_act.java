package com.example.wajdi.bank_app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by wajdi on 04/02/17.
 */

public class recent_act extends Fragment {
    TextView t;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Recent Activity");
        account a = server.getAccount(account_management.chosenAcc);
        String[] tr = server.getAccountTransactions(account_management.chosenAcc);
        for (int i=0;i<tr.length-1;i++) {
            if (tr[i] != null) {
                t.setText(t.getText()+ tr[i].toString()+"\n" );
            }
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.recent_act, container,false);
        t = (TextView) myInflatedView.findViewById(R.id.recentAc);
        return myInflatedView;
    }
}
