package com.isi.pfe.bank_app.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.github.clans.fab.FloatingActionButton;
import com.isi.pfe.bank_app.Classes.Account;
import com.isi.pfe.bank_app.Classes.Account_Management;
import com.isi.pfe.bank_app.Classes.Client_Management;
import com.isi.pfe.bank_app.R;
import com.isi.pfe.bank_app.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    ActionProcessButton btnLogin;
    int retryL,retryC;
    FloatingActionButton itemMap,itemContact,itemExchange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        username=(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);
        btnLogin =(ActionProcessButton) findViewById(R.id.button);
        itemMap = (FloatingActionButton) findViewById(R.id.login_FABItemMap);
        itemContact = (FloatingActionButton) findViewById(R.id.login_FABItemContact);
        itemExchange = (FloatingActionButton) findViewById(R.id.login_FABItemExchange);
        itemMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,AttachedLoginActivity.class);
                intent.putExtra("Fragment",1);
                startActivity(intent);
            }
        });
        itemContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,AttachedLoginActivity.class);
                intent.putExtra("Fragment",2);
                startActivity(intent);
            }
        });
        itemExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,AttachedLoginActivity.class);
                intent.putExtra("Fragment",3);
                startActivity(intent);
            }
        });

    }

    public void login(View view){
        String uname=username.getText().toString();
        String pword=password.getText().toString();
        btnLogin.setMode(ActionProcessButton.Mode.ENDLESS);
        btnLogin.setProgress(1);
        if((uname.equals(""))&&(pword.equals(""))){
            new Account_Management(1);
            retryC=0;
            invokeClientService(1);
        }
        else {
            if (!Utility.validate(uname)) {
                username.setError(getString(R.string.login_invalidEmail));
                btnLogin.setProgress(0);
            }
            else if(!Utility.notNull(uname)) {
                username.setError(getString(R.string.login_emptyEmail));
                btnLogin.setProgress(0);
            }
            else if(!Utility.notNull(pword)) {
                password.setError(getString(R.string.login_emptyPassword));
                btnLogin.setProgress(0);
            }
            else if(pword.trim().length() < 4) {
                password.setError(getString(R.string.login_shortPassword));
                btnLogin.setProgress(0);
            }
            else{
                RequestParams params = new RequestParams();
                        retryL=0;
                        params.put("email", uname);
                        params.put("password", pword);
                        invokeWebService(params);
            }
        }
    }

    public void invokeWebService(final RequestParams params) {
        AsyncHttpClient request = new AsyncHttpClient();
        request.get(getString(R.string.serverIP)+"/WebServices/login/dologin",params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response){
                try{
                    JSONObject obj=new JSONObject(response);
                    if (obj.getBoolean("status")){
                        new Account_Management(obj.getInt("client_code"));
                        retryC=0;
                        invokeClientService(obj.getInt("client_code"));
                    }
                }catch(JSONException e){

                }
            }
            @Override
            public void onFailure(int statusCode, Throwable error, String content){
                retryL++;
                if (retryL < 5)
                    invokeWebService(params);
                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    btnLogin.setProgress(0);
                }
            }
        });
    }
    public void invokeClientService(final int cc) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("client_code", String.valueOf(cc));
        client.get(getString(R.string.serverIP)+"/WebServices/info/getClientInformation",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.e("CI",response);
                    if(obj.getBoolean("status")){
                        new Client_Management(cc,obj.getString("fName"),obj.getString("lName"),obj.getString("email"),obj.getString("address"),obj.getString("phone"),obj.getString("birth_date"),obj.getString("banker"),obj.getInt("id_Banker"));
                        InvokeAccountService();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), getString(R.string.error_falseClient), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_JSON), Toast.LENGTH_LONG).show();
                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,String content) {
                retryC++;
                if (retryC < 5)
                    invokeClientService(cc);
                else {
                    if (statusCode == 404) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_404), Toast.LENGTH_LONG).show();
                    }
                    // When Http response code is '500'
                    else if (statusCode == 500) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_500), Toast.LENGTH_LONG).show();
                    }
                    // When Http response code other than 404, 500
                    else {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_unexpected), Toast.LENGTH_LONG).show();
                    }
                    btnLogin.setProgress(0);
                }
            }
        });


    }
    public void InvokeAccountService(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("client_code", String.valueOf(Account_Management.client_code));
        client.get(getString(R.string.serverIP)+"/WebServices/account/getAccounts",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONObject o;
                    if(obj.getBoolean("status")){
                        System.out.println(obj.getBoolean("status"));
                        int count=obj.getInt("count");
                        Account_Management.listAccount = new ArrayList<>();
                        for(int i=0;i<count;i++) {
                            o = obj.getJSONObject("Account_" + i);
                            Account_Management.listAccount.add(new Account(Account_Management.getAccountNumberString(o.getString("account_number")),o.getString("type"),o.getString("balance"),o.getString("Creation_date"),o.getString("IBAN")));
                        }
                        final Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), getString(R.string.error_falseAccount), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_JSON), Toast.LENGTH_LONG).show();
                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,String content) {
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), getString(R.string.error_404), Toast.LENGTH_LONG).show();
                    InvokeAccountService();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), getString(R.string.error_500), Toast.LENGTH_LONG).show();
                    InvokeAccountService();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getApplicationContext(), getString(R.string.error_unexpected), Toast.LENGTH_LONG).show();
                    InvokeAccountService();
                }
            }
        });

    }

}
