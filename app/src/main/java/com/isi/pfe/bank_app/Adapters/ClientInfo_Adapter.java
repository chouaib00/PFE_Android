package com.isi.pfe.bank_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.isi.pfe.bank_app.Activities.AccountActivity;
import com.isi.pfe.bank_app.Classes.Account_Management;
import com.isi.pfe.bank_app.Classes.AnimationHelper;
import com.isi.pfe.bank_app.Classes.Model;
import com.isi.pfe.bank_app.R;

import java.util.List;

public class ClientInfo_Adapter extends RecyclerView.Adapter{
    private List<Model> listModel;
    private static Context context;

    private static class ClientCardViewHolder extends RecyclerView.ViewHolder{
        TextView nom;
        TextView email;
        TextView adresse;
        TextView tel;
        TextView banker;
        ClientCardViewHolder(View itemview){
            super(itemview);
            nom=(TextView) itemview.findViewById(R.id.clientCard_Name);
            email=(TextView) itemview.findViewById(R.id.clientCard_Email);
            adresse=(TextView) itemview.findViewById(R.id.clientCard_Address);
            tel=(TextView) itemview.findViewById(R.id.clientCard_Tel);
            banker=(TextView) itemview.findViewById(R.id.clientCard_banker);

        }
    }

    private static class AccountCardViewHolder extends RecyclerView.ViewHolder{
        TextView type,num,solde,date;
        String banker,IBAN;
        Button btnSee;
        CardView cardview,mItemDescription;
        boolean expend=false;
        ImageView expand_more;
        ImageView expand_less;

        private AccountCardViewHolder(View itemview){
            super(itemview);
            type=(TextView) itemview.findViewById(R.id.compteCard_Type);
            num=(TextView) itemview.findViewById(R.id.compteCard_NumCompte);
            solde=(TextView) itemview.findViewById(R.id.compteCard_Balance);
            date=(TextView) itemview.findViewById(R.id.compteCard_Date);
            btnSee=(Button) itemview.findViewById(R.id.btnVoir);
            cardview = (CardView) itemview.findViewById(R.id.compte_card);
            mItemDescription =(CardView)itemview.findViewById(R.id.popup_layout);
            expand_more = (ImageView) itemview.findViewById(R.id.expand_more_ic);
            expand_less = (ImageView) itemview.findViewById(R.id.expand_less_ic);
            btnSee.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v){
                    Account_Management.Chosen_Account=Long.parseLong(Account_Management.getAccountNumber(num.getText().toString()));
                    Account_Management.type=type.getText().toString();
                    Account_Management.balance=solde.getText().toString();
                    Account_Management.date=date.getText().toString();
                    Account_Management.banker=banker;
                    Account_Management.IBAN=IBAN;

                    Intent intent = new Intent(context,AccountActivity.class);
                    context.startActivity(intent);
                }
            });
            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!expend) {
                        AnimationHelper.expand(mItemDescription, 500, cardview.getHeight()+30);
                        expend=true;
                        expand_more.setVisibility(View.INVISIBLE);
                        expand_less.setVisibility(View.VISIBLE);
                    }
                    else {
                        AnimationHelper.collapse(mItemDescription, 500, 0);
                        expend=false;
                        expand_more.setVisibility(View.VISIBLE);
                        expand_less.setVisibility(View.INVISIBLE);
                    }

                }
            });
        }
    }


    public ClientInfo_Adapter(List<Model> listModel, Context contextt){
        this.listModel = listModel;
        context=contextt;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case Model.INFO_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_client, parent, false);
                return new ClientCardViewHolder(view);
            case Model.ACCOUNT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_account, parent, false);
                return new AccountCardViewHolder(view);
            default:
                return null;
        }

    }
    @Override
    public int getItemViewType(int position) {

        switch (listModel.get(position).type) {
            case 0:
                return Model.INFO_TYPE;
            case 1:
                return Model.ACCOUNT_TYPE;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {
        Model object=listModel.get(listPosition);
        if(object!=null){
            switch (object.type){
                case Model.INFO_TYPE:{
                    ((ClientCardViewHolder)holder).nom.setText(object.text1);
                    ((ClientCardViewHolder)holder).email.setText(object.text2);
                    ((ClientCardViewHolder)holder).adresse.setText(object.text3);
                    ((ClientCardViewHolder)holder).tel.setText(object.text4);
                    ((ClientCardViewHolder)holder).banker.setText(object.text5);
                    Account_Management.name =object.text1;
                    Account_Management.email=object.text2;
                    Account_Management.address =object.text3;
                    Account_Management.tel=object.text4;
                    Account_Management.banker=object.text5;
                }
                break;
                case Model.ACCOUNT_TYPE:{
                    //TODO:compte en
                    ((AccountCardViewHolder)holder).type.setText("Compte "+object.text2);
                    ((AccountCardViewHolder)holder).num.setText(object.text1);
                    ((AccountCardViewHolder)holder).solde.setText("$"+object.text3);
                    ((AccountCardViewHolder)holder).banker=(object.text4);
                    ((AccountCardViewHolder)holder).date.setText(object.text5);
                    ((AccountCardViewHolder)holder).IBAN=(object.text6);
                }
                break;
            }
        }

    }
    @Override
    public int getItemCount() {
        return listModel.size();
    }

}
