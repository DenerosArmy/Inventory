package com.denerosarmy.inventory;

//import org.apache.commons.lang3.StringUtils;

public class Matcher{

    protected static boolean matches(String s1, String s2){
        if ((s1 == null)||(s2 == null)){
            return false;
        }
        //return StringUtils.getLevenshteinDistance(s1.toLowerCase(), s2.toLowerCase());
        return (s1.toLowerCase()).equals(s2.toLowerCase());
    }

}
