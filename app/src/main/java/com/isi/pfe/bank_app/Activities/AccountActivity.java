package com.isi.pfe.bank_app.Activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.isi.pfe.bank_app.Classes.Account_Management;
import com.isi.pfe.bank_app.Fragments.Account_Info;
import com.isi.pfe.bank_app.Fragments.Request_Money;
import com.isi.pfe.bank_app.Fragments.Transactions;
import com.isi.pfe.bank_app.Fragments.Transfer_Money;
import com.isi.pfe.bank_app.R;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class AccountActivity extends AppCompatActivity {

    private AccountHeader headerResult = null;
    private Drawer result = null;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_account);
        toolbar = (Toolbar) findViewById(R.id.toolbartab);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //new Account_Info();
        FragmentPagerItems pages = new FragmentPagerItems(this);


            pages.add(FragmentPagerItem.of(getString(R.string.tabs_accountInformation), Account_Info.class));
            pages.add(FragmentPagerItem.of(getString(R.string.tabs_transaction), Transactions.class));
            pages.add(FragmentPagerItem.of(getString(R.string.tabs_transferMoney), Transfer_Money.class));
            pages.add(FragmentPagerItem.of(getString(R.string.tabs_requestMoney), Request_Money.class));


        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), pages);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
        final IProfile profile = new ProfileDrawerItem()
                .withName(Account_Management.type)
                .withNameShown(true)
                .withEmail(Account_Management.getAccountNumberString(Long.toString(Account_Management.Chosen_Account)))
                .withIcon(new IconicsDrawable(getApplicationContext())
                        .icon(GoogleMaterial.Icon.gmd_local_atm)
                        .color(getResources().getColor(R.color.material_orange_500))
                        .sizeDp(16));
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withTranslucentStatusBar(false)
                .addProfiles(profile)
                .withSavedInstance(savedInstanceState)
                .build();


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
                                Intent intent = new Intent(AccountActivity.this,MainActivity.class);
                                intent.putExtra("fragment","1");
                                startActivity(intent);
                            }else if (drawerItem.getIdentifier() == 2){
                                Intent intent = new Intent(AccountActivity.this,MainActivity.class);
                                intent.putExtra("fragment",2);
                                startActivity(intent);
                            }else if (drawerItem.getIdentifier() == 3){
                                Intent intent = new Intent(AccountActivity.this,MainActivity.class);
                                intent.putExtra("fragment",3);
                                startActivity(intent);
                            }else if (drawerItem.getIdentifier() == 4){
                                Intent intent = new Intent(AccountActivity.this,MainActivity.class);
                                intent.putExtra("fragment",4);
                                startActivity(intent);
                            }else if (drawerItem.getIdentifier() == 5){
                                Intent intent = new Intent(AccountActivity.this,MainActivity.class);
                                intent.putExtra("fragment",5);
                                startActivity(intent);
                            }else if (drawerItem.getIdentifier() == 6){
                                Intent intent = new Intent(AccountActivity.this,MainActivity.class);
                                intent.putExtra("fragment",6);
                                startActivity(intent);
                            }else if (drawerItem.getIdentifier() == 7){
                                Intent intent = new Intent(AccountActivity.this,MainActivity.class);
                                intent.putExtra("fragment",7);
                                startActivity(intent);
                            }else if (drawerItem.getIdentifier() == 8){
                                Intent intent = new Intent(AccountActivity.this,LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }else if (drawerItem.getIdentifier() == 9) {
                                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                                intent.putExtra("fragment", 9);
                                startActivity(intent);
                            }else if (drawerItem.getIdentifier() == 10) {
                                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                                intent.putExtra("fragment", 10);
                                startActivity(intent);
                            }else if (drawerItem.getIdentifier() == 11) {
                                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                                intent.putExtra("fragment", 11);
                                startActivity(intent);
                            }

                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();
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
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
