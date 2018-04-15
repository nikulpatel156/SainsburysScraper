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
 */
public class FormatterUtils
{
    
     public static BigDecimal cleanseUnitPrice(String unitPrice)
    {
        BigDecimal price = null;

        String value = unitPrice.replaceAll("[^0-9.]", "");
        price = new BigDecimal(value); 
        return price;
    }
    
}
