package com.jce.medapp.Utility;

public class Utility {

    static public boolean checkPassword(String input) {
        String passwordPattern  = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        return input.matches(passwordPattern);
    }


    static public boolean checkMobileNo(String input) {

         String mobilePattern = "[6-9]{1}[0-9]{9}";
         return input.matches(mobilePattern);
     }

     static public boolean checkEmail(String input)
     {
         String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.{1}+[a-z]+";
         return input.matches(emailPattern);
     }
}