package com.isi.pfe.bank_app.Classes;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.isi.pfe.bank_app.R;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class CurrencyItem extends AbstractItem<CurrencyItem, CurrencyItem.ViewHolder> {
    public Drawable country_image;
    public String currency_change;
    public Drawable currency_symbole;
    public String currency_symbole_desc;
    public String currency_change_desc;

    public CurrencyItem withCountry_image(Drawable country_image) {
        this.country_image = country_image;
        return this;
    }

    public CurrencyItem withCurrency_change(String currency_change) {
        this.currency_change = currency_change;
        return this;
    }

    public CurrencyItem withCurrency_symbole(Drawable currency_symbole) {
        this.currency_symbole = currency_symbole;
        return this;
    }

    public CurrencyItem withCurrency_symbole_desc(String currency_symbole_desc) {
        this.currency_symbole_desc = currency_symbole_desc;
        return this;
    }

    public CurrencyItem withCurrency_change_desc(String currency_change_desc) {
        this.currency_change_desc = currency_change_desc;
        return this;
    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return R.id.currency_Item;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.currency_list;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder, payloads);

        //bind our data
        viewHolder.currency_change.setText(currency_change);
        viewHolder.currency_symbole.setImageDrawable(currency_symbole);
        viewHolder.currency_symbole_desc.setText(currency_symbole_desc);
        viewHolder.currency_change_desc.setText(currency_change_desc);
        viewHolder.country_image.setImageDrawable(country_image);

    }

    //reset the view here (this is an optional method, but recommended)
    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.currency_change.setText(null);
        holder.currency_symbole.setImageDrawable(null);
        holder.currency_symbole_desc.setText(null);
        holder.currency_change_desc.setText(null);
        holder.country_image.setImageDrawable(null);
    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView country_image;
        protected TextView currency_change;
        protected ImageView currency_symbole;
        protected TextView currency_symbole_desc;
        protected TextView currency_change_desc;

        public ViewHolder(View view) {
            super(view);
            this.country_image = (ImageView) view.findViewById(R.id.listCountryImage);
            this.currency_change = (TextView) view.findViewById(R.id.currency_change);
            this.currency_symbole = (ImageView) view.findViewById(R.id.listSymbole);
            this.currency_symbole_desc = (TextView) view.findViewById(R.id.listSymboleText);
            this.currency_change_desc = (TextView) view.findViewById(R.id.listCurrencyText);
        }
    }
}
