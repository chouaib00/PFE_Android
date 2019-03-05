package com.isi.pfe.bank_app.Adapters;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.isi.pfe.bank_app.R;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Transactions_Adapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String[]> _listDataHeader;
    private HashMap<String[], String[]> _listDataChild;

    public Transactions_Adapter(Context context, List<String[]> listDataHeader,
                                HashMap<String[], String[]> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String[] childText = (String[]) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.card_transaction_child, null);
        }
        final String[] parentt = (String[]) getGroup(groupPosition);
        MaterialEditText txt = (MaterialEditText) convertView.findViewById(R.id.transactionCardChild_accountNumber);
        txt.setFocusable(true);
        if(Integer.parseInt(parentt[0]) != 3) {
            if (Integer.parseInt(parentt[0]) == 1) {
                txt.setFloatingLabelText(_context.getString(R.string.transactionCard_from_account_number));

            } else {
                txt.setFloatingLabelText(_context.getString(R.string.transactionCard_to_account_number));
            }
            txt.setText(childText[0]);
            txt.setFloatingLabelTextColor(Color.BLACK);
            txt.setTextColor(Color.GRAY);
            MaterialEditText txtListChild2 = (MaterialEditText) convertView.findViewById(R.id.transactionCardChild_OldBalance);
            txtListChild2.setText(childText[1]);
            txtListChild2.setTextColor(Color.GRAY);
            txtListChild2.setFloatingLabelTextColor(Color.BLACK);
            txtListChild2.setFloatingLabelText(_context.getString(R.string.transactionCard_old_balance));
            MaterialEditText txtListChild3 = (MaterialEditText) convertView.findViewById(R.id.transactionCardChild_NewBalance);
            txtListChild3.setText(childText[2]);
            txtListChild3.setTextColor(Color.GRAY);
            txtListChild3.setFloatingLabelTextColor(Color.BLACK);
            txtListChild3.setFloatingLabelText(_context.getString(R.string.transactionCard_new_balance));
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1; //this._listDataChild.get(this._listDataHeader.get(groupPosition)).length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String[] headerTitle = (String[]) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.card_transaction, null);
        }

        TextView type = (TextView) convertView.findViewById(R.id.transactionCard_Type);
        TextView amount = (TextView) convertView.findViewById(R.id.transactionCard_Amount);
        TextView date = (TextView) convertView.findViewById(R.id.transactionCard_Date);
        String dateTxt="",funds="";
        if (Integer.parseInt(headerTitle[0]) != 3) {
            funds = "$" + headerTitle[2];
            if (Integer.parseInt(headerTitle[0]) == 1) {
                funds = "+" + funds;
                amount.setTextColor(Color.GREEN);
            } else if (Integer.parseInt(headerTitle[0]) == 2) {
                funds = "-" + funds;
                amount.setTextColor(Color.RED);
            }
            dateTxt= headerTitle[3];
        }
        type.setText(headerTitle[1]);
        amount.setText(funds);
        date.setText(dateTxt);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}