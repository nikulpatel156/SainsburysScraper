/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sburys.model;

import java.math.BigDecimal;
import org.json.JSONObject;

/**
 *
 * @author NIKUL PATEL
 * This class is used to model a ProductFile.
 */
public class ProductFile
{

    
        
    private String title; //Title of the product
    private int kcalPer100g ; //KCAL value of the product
    private BigDecimal unitPrice ; //Price of the product
    private String description ; // Extended description of the product
    
    /*
    * When converting the product to json these keys values are used.
    */
    private final String JSON_KEY_TITLE = "title";
    private final String JSON_KEY_KCAL = "kcal_per_100g";
    private final String JSON_KEY_UNIT_PRICE = "unit_price";
    private final String JSON_KEY_DESCRIPTION = "description";
    
    public ProductFile()
    {
        
    }

    public ProductFile(String title, int kcalPer100g, BigDecimal unitPrice, String description)
    {
        this.title = title;
        this.kcalPer100g = kcalPer100g;
        this.unitPrice = unitPrice;
        this.description = description;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getKcalPer100g()
    {
        return kcalPer100g;
    }

    public void setKcalPer100g(int kcalPer100g)
    {
        this.kcalPer100g = kcalPer100g;
    }

    public BigDecimal getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    
        
    /*
    * Takes the values of the productfile & places them into a jsonObject
    *
    * @return will be a JSONObject.
    */ 
    public JSONObject toJSONObj()
    {
        JSONObject product = new JSONObject();

        product.put(JSON_KEY_TITLE, this.getTitle());
        product.put(JSON_KEY_KCAL, this.getKcalPer100g());
        product.put(JSON_KEY_UNIT_PRICE, this.getUnitPrice());
        product.put(JSON_KEY_DESCRIPTION, this.getDescription());

        return product;
    }

}
