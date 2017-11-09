package edu.escom.location.util;

public class StringUtil {

    public static String cleanCity(String city){
        return city.replaceAll( "Municipality", "").trim();
    }

}
