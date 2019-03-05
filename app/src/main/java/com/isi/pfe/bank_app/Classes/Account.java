package com.isi.pfe.bank_app.Classes;

public class Account{
    public String num,type,balance,banker,date,IBAN;

    public Account(String num, String type, String balance, String date, String IBAN) {
        this.num = num;
        this.type = type;
        this.balance = balance;
        //TODO:banker
        this.banker = Client_Management.Banker;
        this.date = date;
        this.IBAN = IBAN;
    }
}
