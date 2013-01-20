package com.denerosarmy.inventory;

public class Matcher{

    protected static boolean matches(String s1, String s2){
        if ((s1 == null)||(s2 == null)){
            return false;
        }
        return (s1.toLowerCase()).equals(s2.toLowerCase());
    }

}
