package com.isi.pfe.bank_app.Activities;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;

import com.isi.pfe.bank_app.Classes.Account;
import com.isi.pfe.bank_app.Classes.Account_Management;
import com.isi.pfe.bank_app.Fragments.*;
import com.isi.pfe.bank_app.R;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private AccountHeader headerResult = null;
    private Drawer result = null;
    Toolbar toolbar;
    Fragment fragment;
    FragmentTransaction ft;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //init menu
        List<IProfile> listprofile = new ArrayList<>();
        for(int i=0;i< Account_Management.listAccount.size();i++){
            Account a = Account_Management.listAccount.get(i);
            //TODO:compte EN
            IProfile profile = new ProfileDrawerItem()
                    .withName("Compte "+a.type)
                    .withNameShown(true)
                    .withEmail(a.num);
            listprofile.add(profile);
        }
        Account_Management.Chosen_Account = Long.parseLong(Account_Management.getAccountNumber(listprofile.get(0).getEmail().toString()));
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withTranslucentStatusBar(false)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        Account_Management.Chosen_Account = Long.parseLong(Account_Management.getAccountNumber(profile.getEmail().toString()));
                        if ((fragment instanceof Transactions) || (fragment instanceof Transfer_Money)){
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.detach(fragment);
                            ft.attach(fragment);
                            ft.commit();
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        for(int i=0;i<listprofile.size();i++){
            headerResult.addProfile(listprofile.get(i),i);
        }


                        result = new DrawerBuilder()
                                .withActivity(this)
                                .withToolbar(toolbar)
                                .withTranslucentStatusBar(false)
                                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                                .addDrawerItems(
                                        new PrimaryDrawerItem().withName(R.string.drawer_item_account_info).withIcon(GoogleMaterial.Icon.gmd_account).withIdentifier(1),
                                        new SectionDrawerItem().withName(R.string.drawer_item_section_money),
                                        new PrimaryDrawerItem().withName(R.string.drawer_item_transfer_money).withIcon(FontAwesome.Icon.faw_money).withIdentifier(2),
                                        new PrimaryDrawerItem().withName(R.string.drawer_item_pay_bills).withIcon(FontAwesome.Icon.faw_credit_card).withIdentifier(3),
                                        new PrimaryDrawerItem().withName(R.string.drawer_item_see_transaction).withIcon(FontAwesome.Icon.faw_arrows_h).withIdentifier(4),
                                        new PrimaryDrawerItem().withName(R.string.drawer_item_statsGraphic).withIcon(FontAwesome.Icon.faw_book).withIdentifier(10),
                                        new SectionDrawerItem().withName(R.string.drawer_item_more),
                                        new PrimaryDrawerItem().withName(R.string.drawer_item_exchange).withIcon(FontAwesome.Icon.faw_arrows_h).withIdentifier(9),
                                        new PrimaryDrawerItem().withName(R.string.drawer_item_credit).withIcon(FontAwesome.Icon.faw_car).withIdentifier(11),
                                        new SectionDrawerItem().withName(R.string.drawer_item_connect),
                                        new PrimaryDrawerItem().withName(R.string.drawer_item_find_atm).withIcon(FontAwesome.Icon.faw_building).withIdentifier(5),
                                        new PrimaryDrawerItem().withName(R.string.drawer_item_send_request).withIcon(FontAwesome.Icon.faw_hand_paper_o).withIdentifier(6),
                                        new PrimaryDrawerItem().withName(R.string.drawer_item_contact_us).withIcon(FontAwesome.Icon.faw_bookmark).withIdentifier(7),
                                        new DividerDrawerItem(),
                                        new PrimaryDrawerItem().withName(R.string.drawer_item_disconnect).withIcon(FontAwesome.Icon.faw_sign_out).withIdentifier(8)
                                        ) // add the items we want to use with our Drawer
                                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                        if (drawerItem!=null){
                                            if (drawerItem.getIdentifier() == 1){
                                                ft = getSupportFragmentManager().beginTransaction();
                                                fragment = new Client_Informations();
                                                ft.replace(R.id.content_main, fragment,"client_information");
                                                ft.addToBackStack(null);
                                                ft.commit();
                                            }else if (drawerItem.getIdentifier() == 2){
                                                ft = getSupportFragmentManager().beginTransaction();
                                                fragment = new Transfer_Money();
                                                Bundle b = new Bundle();
                                                b.putBoolean("toolbar",true);
                                                fragment.setArguments(b);
                                                ft.replace(R.id.content_main, fragment,"transfer_money");
                                                ft.addToBackStack(null);
                                                ft.commit();
                                            }else if (drawerItem.getIdentifier() == 3){
                                                ft = getSupportFragmentManager().beginTransaction();
                                                fragment = new Pay_Bills();
                                                ft.replace(R.id.content_main, fragment,"pay_bills");
                                                ft.addToBackStack(null);
                                                ft.commit();
                                            }else if (drawerItem.getIdentifier() == 4){
                                                ft = getSupportFragmentManager().beginTransaction();
                                                fragment = new Transactions();
                                                Bundle b = new Bundle();
                                                b.putBoolean("toolbar",true);
                                                fragment.setArguments(b);
                                                ft.replace(R.id.content_main, fragment,"transactions");
                                                ft.addToBackStack(null);
                                                ft.commit();
                                            }else if (drawerItem.getIdentifier() == 5){
                                                ft = getSupportFragmentManager().beginTransaction();
                                                fragment = new Find_ATM();
                                                ft.replace(R.id.content_main, fragment,"find_atm");
                                                ft.addToBackStack(null);
                                                ft.commit();
                                            }else if (drawerItem.getIdentifier() == 6){
                                                ft = getSupportFragmentManager().beginTransaction();
                                                fragment = new Send_Request();
                                                ft.replace(R.id.content_main, fragment,"send_request");
                                                ft.addToBackStack(null);
                                                ft.commit();
                                            }else if (drawerItem.getIdentifier() == 7){
                                                ft = getSupportFragmentManager().beginTransaction();
                                                fragment = new Contact();
                                                ft.replace(R.id.content_main, fragment,"contact");
                                                ft.addToBackStack(null);
                                                ft.commit();
                                            }else if (drawerItem.getIdentifier() == 8){
                                                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            }else if (drawerItem.getIdentifier() == 9) {
                                                ft = getSupportFragmentManager().beginTransaction();
                                                fragment = new Exchange();
                                                ft.replace(R.id.content_main, fragment,"exchange");
                                                ft.addToBackStack(null);
                                                ft.commit();
                                            }else if (drawerItem.getIdentifier() == 10) {
                                                ft = getSupportFragmentManager().beginTransaction();
                                                fragment = new my_stats();
                                                ft.replace(R.id.content_main, fragment,"my_stats");
                                                ft.addToBackStack(null);
                                                ft.commit();
                                            }else if (drawerItem.getIdentifier() == 11) {
                                                ft = getSupportFragmentManager().beginTransaction();
                                                fragment = new Credit_Simulator();
                                                ft.replace(R.id.content_main, fragment,"credit_simulator");
                                                ft.addToBackStack(null);
                                                ft.commit();
                                            }

                                        }
                                        return false;
                                    }
                                })
                                .withSavedInstance(savedInstanceState)
                                .withShowDrawerOnFirstLaunch(true)
                                .build();
        ft = getSupportFragmentManager().beginTransaction();
        int f = getIntent().getIntExtra("fragment",1);
        switch (f){
            case 1:fragment = new Client_Informations();
                break;
            case 2:fragment = new Transfer_Money();
                break;
            case 3:fragment = new Pay_Bills();
                break;
            case 4:fragment = new Transactions();
                break;
            case 5:fragment = new Find_ATM();
                break;
            case 6:fragment = new Send_Request();
                break;
            case 7:fragment = new Contact();
                break;
            case 9:fragment = new Exchange();
                break;
            case 10:fragment = new my_stats();
                break;
            case 11:fragment = new Credit_Simulator();
                break;
        }
        ft.replace(R.id.content_main, fragment);
        ft.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        switch (item.getItemId()) {
            case android.R.id.home:
                result.openDrawer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
