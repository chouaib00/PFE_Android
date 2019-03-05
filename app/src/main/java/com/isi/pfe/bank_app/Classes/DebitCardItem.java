package com.isi.pfe.bank_app.Classes;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.isi.pfe.bank_app.R;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import java.util.List;

/**
 * Created by nader on 3/13/17.
 */

public class DebitCardItem extends AbstractItem<DebitCardItem, DebitCardItem.ViewHolder> {
    public String title;
    public Drawable icon;



    public DebitCardItem withText(String title){
        this.title=title;
        return this;
    }
    public DebitCardItem withIcon(Drawable icon){
        this.icon=icon;
        return this;
    }


    //The unique ID for this type of item
    @Override
    public int getType() {
        return R.id.debit_card_item;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.debit_card_item;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder, payloads);

        //bind our data
        viewHolder.title.setText(title);
        viewHolder.icon.setImageDrawable(icon);

    }

    //reset the view here (this is an optional method, but recommended)
    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.title.setText(null);
        holder.icon.setImageDrawable(null);

    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected ImageView icon;

        public ViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.debit_card_item_title);
            this.icon = (ImageView) view.findViewById(R.id.debit_card_item_icon);
        }
    }
}
