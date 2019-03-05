package com.isi.pfe.bank_app.Classes;

import android.content.Context;

public class Client_Management {
     public static int code_client,id_Banker;
     public static String fName, lName,email,address, phone,birth_date,Banker;
     public static Context context;
    public static boolean usable=false;

    public Client_Management(int code_client, String fname, String lname, String email, String address, String phone,String bd,String Banker,int id_Banker) {
        Client_Management.code_client = code_client; Client_Management.fName = fname; Client_Management.lName = lname; Client_Management.email = email;
        Client_Management.address = address; Client_Management.phone = phone; Client_Management.birth_date=bd;Client_Management.Banker=Banker; Client_Management.id_Banker=id_Banker;
        usable = true;
    }

    public static String getcName() {
        return lName.substring(0,1).toUpperCase() + "." + fName;
    }

}
