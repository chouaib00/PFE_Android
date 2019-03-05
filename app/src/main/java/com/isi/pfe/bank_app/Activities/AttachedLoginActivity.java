package com.isi.pfe.bank_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.isi.pfe.bank_app.Fragments.Contact;
import com.isi.pfe.bank_app.Fragments.Exchange;
import com.isi.pfe.bank_app.Fragments.Find_ATM;
import com.isi.pfe.bank_app.R;


public class AttachedLoginActivity extends AppCompatActivity {
    Fragment fragment;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        int frg = intent.getIntExtra("Fragment",0);
        Bundle bundle = new Bundle();
        bundle.putBoolean("toolbar", false);
        switch (frg) {
            case 1:
                ft = getSupportFragmentManager().beginTransaction();
                fragment = new Find_ATM();
                fragment.setArguments(bundle);
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 2:
                ft = getSupportFragmentManager().beginTransaction();
                fragment = new Contact();
                fragment.setArguments(bundle);
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 3:
                ft = getSupportFragmentManager().beginTransaction();
                fragment = new Exchange();
                fragment.setArguments(bundle);
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;
            default:
                intent = new Intent(AttachedLoginActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
