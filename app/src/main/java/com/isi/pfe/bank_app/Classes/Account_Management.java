package com.isi.pfe.bank_app.Classes;


import android.content.Context;
import android.widget.Toast;

import com.isi.pfe.bank_app.R;

import java.util.ArrayList;
import java.util.List;

public class Account_Management  {
    public static int client_code;
    public static long Chosen_Account= -1;
    public static String name,email,address,tel,type,balance,banker,date,IBAN="55555";
    public static List<Account> listAccount = new ArrayList<>();
    public Account_Management(int cc){
        client_code =cc;
    }

    public static String getAccountNumberString(String account_number) {
        if(account_number.equals("0")) return "Bank";
        if(account_number.length() != 16) return "Error";
        String an;
        an = account_number.substring(0,4) + " " + account_number.substring(4,8) + " " + account_number.substring(8,12) + " " +account_number.substring(12,16);
        return an;
    }
    public static String getAccountNumber(String account_number) {
        String an;
        an = account_number.replaceAll(" ","");
        return an;
    }

    public static String getAccountNumberString(Context context) {
        if (Chosen_Account != -1) return String.valueOf(Chosen_Account);
        Toast.makeText(context, context.getString(R.string.error_chooseAccount), Toast.LENGTH_SHORT).show();
        return "";
    }


}



