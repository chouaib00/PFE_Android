package com.example.wajdi.bank_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by wajdi on 06/02/17.
 */

public class account_management extends Activity {
    public static int code_client;
    public static account[] compte_client;
    public static int chosenAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_acc);

        String email="";
        Bundle b = getIntent().getExtras();
        if(b!=null) email= b.getString("email");
        Log.e("AM",email);
        server s = new server();
        code_client = s.getCodeClient(email);
        compte_client = s.getClientAccounts(code_client);
        //setContentView(R.layout.activity_main);

    }
    public void onBtnClicked(View v) {
        chosenAcc = compte_client[0].getNumAcc();
        Log.e("btn1",String.valueOf(chosenAcc));
        startMain();
    }
    public void onBtnClicked2(View v) {
        if (compte_client[1] != null) {
            chosenAcc= compte_client[1].getNumAcc();
            startMain();
        }
    }

    public void startMain() {
        Intent intent = new Intent(account_management.this, MainActivity.class);
        startActivity(intent);
    }
}
