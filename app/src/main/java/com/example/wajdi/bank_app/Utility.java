package com.example.wajdi.bank_app;

/**
 * Created by wajdi on 06/02/17.
 */

public class Utility {


    /**
     * Validate Email with regular expression
     *
     * @param email
     * @return true for Valid Email and false for Invalid Email
     */
    public static boolean validate(String email) {
        return (!email.isEmpty() || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());

    }
    /**
     * Checks for Null String object
     *
     * @param txt
     * @return true for not null and false for null String object
     */
    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length()>0 ? true: false;
    }
}