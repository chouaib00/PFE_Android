package com.isi.pfe.bank_app.Classes;


public class Model {
    public static final int INFO_TYPE = 0;
    public static final int ACCOUNT_TYPE = 1;

    public int type;
    public String text1;
    public String text2;
    public String text3;
    public String text4;
    public String text5;
    public String text6;

    public Model(int type, String text1, String text2, String text3, String text4, String text5) {
        this.type = type;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.text4 = text4;
        this.text5 = text5;
    }

    public Model(int type, String text1, String text2, String text3, String text4, String text5,String text6) {
        this.type = type;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.text4 = text4;
        this.text5 = text5;
        this.text6 = text6;
    }
}
