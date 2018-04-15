/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sburys.scraper.util;

import java.math.BigDecimal;

/**
 *
 * @author NIKUL PATEL
 * Class to place commonly used utils.
 */
public class FormatterUtils
{
    
     /*
        @param unitPrice - Takes in a Alphanumeric value , removes any non numberic values whilst leaving decimal places intact.
               e.g. "£1.75/unit" will be returned as 1.75
        @return BigDecimal value 
    */
    public static BigDecimal cleanseUnitPrice(String unitPrice)
    {
        BigDecimal price = null;

        String value = unitPrice.replaceAll("[^0-9.]", "");
        price = new BigDecimal(value); 
        return price;
    }
    
                
    /*
        @param text - Alphanumeric text that we want to remove non numeric values from.
        @return int - returns an integer        
   
        e.g. "33kcal" will be returned as int 33.
    */
    public static int cleanseAlphaNumToInt(String text)
    { 

        String value = text.replaceAll("[^0-9]", "");
        
        return Integer.parseInt(value);
    } 
    
}
