package com.isi.pfe.bank_app.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.isi.pfe.bank_app.Classes.DebitCardItem;
import com.isi.pfe.bank_app.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

public class CreditCard_request extends Fragment {
    Toolbar toolbar;
    RecyclerView rv;

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Debit Card Request");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        FastItemAdapter<DebitCardItem> adapter = new FastItemAdapter();
        String[] listDebitCard = getResources().getStringArray(R.array.debit_card_type);
        for(int i=0;i<listDebitCard.length;i++){
            DebitCardItem item = new DebitCardItem()
                    .withText(listDebitCard[i])
                    .withIcon(new IconicsDrawable(getContext())
                    .icon(FontAwesome.Icon.faw_credit_card)
                    .sizeDp(24));
            adapter.add(item);
        }

        adapter.withOnClickListener(new FastAdapter.OnClickListener<DebitCardItem>() {
            @Override
            public boolean onClick(View v, IAdapter<DebitCardItem> adapter, DebitCardItem item, int position) {
                Bundle b;
                Fragment nextfrag;
                switch (position){
                    case 0:
                        b = new Bundle();
                        b.putString("ftitle","Carte Gold");
                        b.putString("title",getResources().getString(R.string.carte_gold_title));
                        b.putString("desc",getResources().getString(R.string.carte_gold_desc));
                        b.putString("subTitle",getResources().getString(R.string.carte_gold_subTitle));
                        b.putString("list",getResources().getString(R.string.carte_gold_list));
                        nextfrag= new DebitCardDescription();
                        nextfrag.setArguments(b);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_main, nextfrag,"creditCard")
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 1:
                        b = new Bundle();
                        b.putString("ftitle","Carte Visa");
                        b.putString("title",getResources().getString(R.string.carte_visa_title));
                        b.putString("desc",getResources().getString(R.string.carte_visa_desc));
                        b.putString("subTitle",getResources().getString(R.string.carte_visa_subTitle));
                        b.putString("list",getResources().getString(R.string.carte_visa_list));
                        nextfrag = new DebitCardDescription();
                        nextfrag.setArguments(b);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_main, nextfrag,"creditCard")
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 2:
                        b = new Bundle();
                        b.putString("ftitle","Carte Salaire +");
                        b.putString("title",getResources().getString(R.string.carte_salaire_title));
                        b.putString("desc",getResources().getString(R.string.carte_salaire_desc));
                        b.putString("subTitle",getResources().getString(R.string.carte_salaire_subTitle));
                        b.putString("list",getResources().getString(R.string.carte_salaire_list));
                        nextfrag = new DebitCardDescription();
                        nextfrag.setArguments(b);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_main, nextfrag,"creditCard")
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 3:
                        b = new Bundle();
                        b.putString("ftitle","Carte CIB");
                        b.putString("title",getResources().getString(R.string.carte_CIB_title));
                        b.putString("desc",getResources().getString(R.string.carte_CIB_desc));
                        b.putString("subTitle",getResources().getString(R.string.carte_CIB_subTitle));
                        b.putString("list",getResources().getString(R.string.carte_CIB_list));
                        nextfrag = new DebitCardDescription();
                        nextfrag.setArguments(b);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_main, nextfrag,"creditCard")
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 4:
                        b = new Bundle();
                        b.putString("ftitle","Carte Iddikhar");
                        b.putString("title",getResources().getString(R.string.carte_Iddikhar_title));
                        b.putString("desc",getResources().getString(R.string.carte_Iddikhar_desc));
                        b.putString("subTitle",getResources().getString(R.string.carte_Iddikhar_subTitle));
                        b.putString("list",getResources().getString(R.string.carte_Iddikhar_list));
                        nextfrag = new DebitCardDescription();
                        nextfrag.setArguments(b);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_main, nextfrag,"creditCard")
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 5:
                        b = new Bundle();
                        b.putString("ftitle","Carte Voyage");
                        b.putString("title",getResources().getString(R.string.carte_Voyage_title));
                        b.putString("desc",getResources().getString(R.string.carte_Voyage_desc));
                        b.putString("subTitle",getResources().getString(R.string.carte_Voyage_subTitle));
                        b.putString("list",getResources().getString(R.string.carte_Voyage_list));
                        nextfrag = new DebitCardDescription();
                        nextfrag.setArguments(b);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_main, nextfrag,"creditCard")
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 6:
                        b = new Bundle();
                        b.putString("ftitle","Carte Mobicash");
                        b.putString("title",getResources().getString(R.string.carte_Mobicash_title));
                        b.putString("desc",getResources().getString(R.string.carte_Mobicash_desc));
                        b.putString("subTitle",getResources().getString(R.string.carte_Mobicash_subTitle));
                        b.putString("list",getResources().getString(R.string.carte_Mobicash_list));
                        nextfrag = new DebitCardDescription();
                        nextfrag.setArguments(b);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_main, nextfrag,"creditCard")
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 7:
                        b = new Bundle();
                        b.putString("ftitle","Carte Jeunes");
                        b.putString("title",getResources().getString(R.string.carte_Jeunes_title));
                        b.putString("desc",getResources().getString(R.string.carte_Jeunes_desc));
                        b.putString("subTitle",getResources().getString(R.string.carte_Jeunes_subTitle));
                        b.putString("list",getResources().getString(R.string.carte_Jeunes_list));
                        nextfrag = new DebitCardDescription();
                        nextfrag.setArguments(b);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_main, nextfrag,"creditCard")
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 8:
                        b = new Bundle();
                        b.putString("ftitle","Carte Tawa Tawa");
                        b.putString("title",getResources().getString(R.string.carte_Tawa_title));
                        b.putString("desc",getResources().getString(R.string.carte_Tawa_desc));
                        b.putString("subTitle",getResources().getString(R.string.carte_Tawa_subTitle));
                        b.putString("list",getResources().getString(R.string.carte_Tawa_list));
                        nextfrag = new DebitCardDescription();
                        nextfrag.setArguments(b);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_main, nextfrag,"creditCard")
                                .addToBackStack(null)
                                .commit();
                        break;
                }
                return false;
            }
        });


        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(rv.getContext(),
                new LinearLayoutManager(getContext()).getOrientation());
        rv.addItemDecoration(mDividerItemDecoration);
        rv.setAdapter(adapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.fragment_credit_card_request, container, false);
        toolbar = (Toolbar) myInflatedView.findViewById(R.id.toolbardebitcard);
        rv = (RecyclerView) myInflatedView.findViewById(R.id.credit_card_rv);
        return myInflatedView;
    }


}
