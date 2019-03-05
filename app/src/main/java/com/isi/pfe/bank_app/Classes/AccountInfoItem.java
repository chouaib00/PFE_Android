package com.isi.pfe.bank_app.Classes;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.isi.pfe.bank_app.R;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class AccountInfoItem extends AbstractItem<AccountInfoItem, AccountInfoItem.ViewHolder> {
    public Drawable icon;
    public String title;
    public String subTitle;
    public int color;



    public AccountInfoItem withIcon(Drawable icon){
        this.icon=icon;
        return this;
    }

    public AccountInfoItem withTitle(String title){
        this.title=title;
        return this;
    }
    public AccountInfoItem withsubTitle(String subTitle){
        this.subTitle = subTitle;
        return this;
    }
    public AccountInfoItem withColor(int color){
        this.color = color;
        return this;
    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return R.id.AccountInfoItem;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.account_info_item;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder, payloads);

        //bind our data
        viewHolder.icon.setImageDrawable(icon);
        viewHolder.title.setText(title);
        viewHolder.subTitle.setText(subTitle);
        viewHolder.subTitle.setTextColor(color);

    }

    //reset the view here (this is an optional method, but recommended)
    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.icon.setImageDrawable(null);
        holder.title.setText(null);
        holder.subTitle.setText(null);

    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView icon;
        protected TextView title;
        protected TextView subTitle;

        public ViewHolder(View view) {
            super(view);
            this.icon = (ImageView) view.findViewById(R.id.icon_item);
            this.title = (TextView) view.findViewById(R.id.title_item);
            this.subTitle = (TextView) view.findViewById(R.id.subTitle_item);

        }
    }
}

