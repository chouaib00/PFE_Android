package com.example.wajdi.bank_app;

import android.util.Log;


/**
 * Created by wajdi on 04/02/17.
 */

public class server {
    //TODO: BD
    static account[] a = new account[3];
    static client[] c = new client[2];
    static transaction[] t = new transaction[6];
    account ac1 = new account(1,1,123,1920);
    account ac2 = new account(2,2,4343,2830.12);
    account ac3 = new account(3,1,123,2200);
    client c1 = new client(1,"a@b.c","123456","khattel","Wajdi","10-Tunis","1234234");
    client c2 = new client(2,"d@e.f","123456","Somrani","Nader","102-Tunis","1234234");
    transaction t1 = new trans_send(1,2,100);
    transaction t1r = new trans_receive(2,1,100);
    transaction t2 = new trans_send(2,1,500);
    transaction t2r = new trans_receive(1,2,500);
    transaction t3 = new trans_send(3,2,170);
    transaction t3r = new trans_receive(2,3,170);

    public server() {
        a[0] = ac1;a[1] = ac2;a[2] = ac3;
        c[0] = c1;c[1] = c2;
        t[0] = t1;t[1] = t1r;t[2]=t2;t[3]=t2r;t[4]=t3;t[5]=t3r;
    }

    public int getCodeClient(String email) {
        int i =0;
        while (!(c[i].getEmail().equals(email)) && (i < a.length -1)) i++;
        if (c[i].getEmail().equals(email)) return c[i].getCode_client();
        return -1;
    }

    public static client getClient(int cc) {
        int i =0;
        while ((c[i].getCode_client() != cc) && (i < a.length -1)) i++;
        if (c[i].getCode_client() == cc) return c[i];
        return null;
    }

    public account[] getClientAccounts(int cc) {
        account[] acs = new account[3];
        int i = 0;
        for (account account : a) {
            if (account.getCode_client() == cc) {
                acs[i] = account;
                i++;
            }
        }
        return acs;
    }

    public static account getAccount(int numAcc) {
        int i =0;
        while ((a[i].getNumAcc() != numAcc) && (i < a.length -1)) i++;
        if (a[i].getNumAcc() == numAcc) return a[i];
        return null;
    }

    public static String[] getAccountTransactions(int nc) {
        String[] trs = new String[5];
        int i=0;
        for (transaction tr : t) {
            if (tr.num_compte == nc) {
                trs[i] = tr.toString();
                i++;
            }
        }
        return trs;
    }

}

 class client {
     private int code_client;
     private String nom,prenom,email,address,num_tel,mdp;
     //private Date date_naissance;

     public client(int cc, String email, String mdp, String nom,String prenom, String address, String num_tel) {
         this.code_client=cc;
         this.email = email;
         this.mdp = mdp;
         this.nom = nom;
         this.prenom = prenom;
         this.address = address;
         this.num_tel=num_tel;
         //this.date_naissance = dn;
     }

     public int getCode_client() {
         return code_client;
     }

     public String getNom() {
         return nom;
     }

     public String getPrenom() {
         return prenom;
     }

     public String getEmail() {
         return email;
     }

     public String getAddress() {
         return address;
     }

     public String getNum_tel() {
         return num_tel;
     }

     public String getMdp() {
         return mdp;
     }

     public String getcName() {
         return this.nom.substring(0,1).toUpperCase() + "." + this.prenom;
     }

 }


 class account {
      private int numAcc,code_client,pin;
      private double solde;
    //  private Date date_ouverture;
     public account(int numc, int cc, int pin, double solde) {
         this.numAcc=numc;
         this.code_client=cc;
         this.pin=pin;
         this.solde=solde;
        // this.date_ouverture=dov;
     }

     public void addMoney(double nMoney) {
         this.solde += nMoney;
     }
     public void remMoney(double nMoney) {
         this.solde -= nMoney;
     }

     public int getNumAcc() {
         return numAcc;
     }

     public int getCode_client() {
         return code_client;
     }

     public int getPin() {
         return pin;
     }

     public double getSolde() {
         return solde;
     }

 }

class transaction {
    protected int num_compte;
    protected double montant;
    //protected Date date;

    transaction(int num_compte,double ammount) {
        this.num_compte=num_compte;
        this.montant=ammount;
       // this.date=date;
    }
    public String toString() {
        return "error";
    }
}

class trans_send extends transaction {
    private int nc_sTo;
    public trans_send(int numc, int to, double montant) {
        super(numc,montant);
        this.nc_sTo=to;
    }

    public int getNc_sTo() {
        return nc_sTo;
    }

    public String toString(){
        return "Num Acc: " + num_compte + " sent " + montant + " to " + nc_sTo;
    }
}


class trans_receive extends transaction {
    private int nc_sFrom;
    public trans_receive(int numc, int from, double montant) {
        super(numc,montant);
        this.nc_sFrom=from;
    }

    public int getNc_sFrom() {
        return nc_sFrom;
    }

    public String toString(){
        return "Num Acc: " + num_compte + " received " + montant + " from " + nc_sFrom;
    }
}