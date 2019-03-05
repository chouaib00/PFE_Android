package com.isi.pfe.bank_app.Classes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.isi.pfe.bank_app.R;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;


public class BaseItem extends AbstractItem<BaseItem, BaseItem.ViewHolder> {
    public Drawable flag;
    public String flag_desc;
    public String id;
    public Context context;
    public int flagID;

    public BaseItem withContext(Context context){
        this.context = context;
        return this;
    }

    public BaseItem withFlag(Drawable flag){
        this.flag=flag;
        return this;
    }
    public BaseItem withFlagDesc(String desc){
        this.flag_desc=desc;
        return this;
    }
    public BaseItem withID(String id){
        this.id=id;
        return this;
    }
    public BaseItem withFlagId(int id){
        this.flagID = id;
        return this;
    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return R.id.base_item;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.base_item;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder, payloads);

        //bind our data
        viewHolder.flag.setImageDrawable(flag);
        viewHolder.flag_desc.setText(flag_desc);


    }

    //reset the view here (this is an optional method, but recommended)
    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.flag.setImageDrawable(null);
        holder.flag_desc.setText(null);

    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView flag;
        protected TextView flag_desc;

        public ViewHolder(View view) {
            super(view);
            this.flag = (ImageView) view.findViewById(R.id.flag);
            this.flag_desc = (TextView) view.findViewById(R.id.flag_desc);

        }
    }
}
