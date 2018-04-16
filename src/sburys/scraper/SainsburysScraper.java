/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sburys.scraper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sburys.model.ProductFile;
import sburys.scraper.util.FormatterUtils;
import sburys.scraper.util.SiteScraper;
import sburys.scraper.util.impl.SiteScraperImpl;

/**
 *
 * @author NIKUL PATEL
 * This will scrape the sainburys site to retrieve product information.
 */
public class SainsburysScraper
{

    //URL of the site that will be scraped.
    private final String MAIN_SITE_URL = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";

    // DIV tag that the products are wrapped in.
    private final String HTML_LOOKUP_TAG_PRODUCTS = "div[id*=product] div.productInfo";

    // Stores all the ProductFile models that are retrieved when scraping the site.
    private List<ProductFile> productFileList = new ArrayList<>();
    
    // SiteScraper instance that can be used to scrape the site.
    private SiteScraper scraper;

    public SainsburysScraper()
    {
        
        // Initialize variables
        scraper = new SiteScraperImpl();

        scrapeSite(); 
        
        // Convert all the scraped products to JSON
        convertDataToJSON();
         

    }
    
    public void scrapeSite()
    {
        resetVariables();
        
        // This holds the main site as a document  - Site will be based on the URL - MAIN_SITE_URL.
        Document mainSiteDocument = null;
        try
        {
            mainSiteDocument = scraper.getSiteAsDocument(MAIN_SITE_URL);
        }
        catch (IOException ex)
        {
            Logger.getLogger(SainsburysScraper.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        if(mainSiteDocument == null)
        {
            System.out.println("Error : mainSiteDocument is null. Unable to scrape product details.");
            return;
        }

        // Scrape the site looking for products , should return a list of product elements
        Elements productItemsElements = scraper.findSiteElements(mainSiteDocument, HTML_LOOKUP_TAG_PRODUCTS);
        System.out.println("Found: " + productItemsElements.size() + " products.");
        
        // Pass the product items to be scraped so each products details can be scraped.
        scrapeProductItems(productItemsElements);
        
    }
    
    private void resetVariables()
    { 
        this.setProductFileList(new ArrayList<ProductFile>());        
    }

    /*
        @param productItemsElements - List of product elements
    */
    private void scrapeProductItems(Elements productItemsElements)
    {
        // Depending on how many product were found iterate through each one to scrape the products details.
        for (Element productElement : productItemsElements)
        {
            //The scraped product data will be store into a ProductFile model.
            ProductFile prodFile = new ProductFile();
            String productTitle = productElement.selectFirst("div.productInfo div.productNameAndPromotions").text(); // Get the products title from within the div tag.
            String productDetailsURL = productElement.select("a[href]").get(0).attr("abs:href"); // Get the URL on the product which will contain a URL to the page where the advanced details of a product can be scraped from.

            // Set the products title based on what was found.
            prodFile.setTitle(productTitle);
            System.out.println("Product : " + productTitle);
            
            // Pass the URL & product file so the advanced product details can be scraped.
            scrapeProductAdavancedDetails(productDetailsURL, prodFile);
            
            // Add the product model to the list
            this.getProductFileList().add(prodFile);
            
            

        }
        
        
            

    }
    
    /*
        This method will go though the found Products , convert them to JSON Data & add them to a JSONArray.
    */
    private void convertDataToJSON()
    {
        // Used to hold all the convert product files
        JSONArray jsonItemsArr = new JSONArray();
        BigDecimal priceTotal = BigDecimal.ZERO; // Keep a hold of the price totals to append it to the JSON data.

        // Go through each scraped product
        for (ProductFile prod : this.getProductFileList())
        {
            // Retrieve the price of each Product & add it to the running total "priceTotal"
            priceTotal = priceTotal.add(prod.getUnitPrice());
            // Call the method on the product model to retrieve the product as a JSON Object.
            jsonItemsArr.put(prod.toJSONObj());

        }

        //Store the scraped products in a JSON Object.
        JSONObject products = new JSONObject();
        
        
        products.put("results", jsonItemsArr); // Add in all the converted product items  
        products.put("total", priceTotal); // Add the price
        

        System.out.println("Results:");
        System.out.println();
        System.out.println(products);
    }

    /*
        @param prodDetailsURL - URL of the page where the advanced details of the product can be scraped from.
        @param prodFile - ProductFile that will be used to store the found products data.
    */
    private void scrapeProductAdavancedDetails(String prodDetailsURL, ProductFile prodFile)
    {
        // Holds the subsite as a document.
        Document subSiteDocument = null;
        try
        {
            // Use the scraper to return the site as a document.
            subSiteDocument = scraper.getSiteAsDocument(prodDetailsURL);
             
        }
        catch (IOException ex)
        {
            Logger.getLogger(SainsburysScraper.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        if(subSiteDocument == null)
        {
            System.out.println("Error : subSiteDocument is null. Unable to scrape product details.");
            return;
        }

        //<div class="productSummary"> - Unit price is contained with this tag
        Element produtSummaryElement = scraper.findSiteElement(subSiteDocument, "div.productSummary");

        /*
            Unit price is within a <p> tag called "pricePerUnit". Findes the first matching element. 
            The price is likely in the format "£2.50/unit" so pass the value to the FormatterUtils.cleanseUnitPrice() to return just the price as a BigDecimal.
        */
        BigDecimal unitPrice = FormatterUtils.cleanseUnitPrice(produtSummaryElement.selectFirst("p.pricePerUnit").text());
        
        String description = "";
        
         //only get the first p tag that contains descrption to scrape the first line only
        Elements descriptions = scraper.findSiteElements(subSiteDocument, "div.productText p");
        for (Element desc : descriptions)
        {
            // If the descpription is not null or Empty then we have the description
            if(desc.text() != null && !desc.text().isEmpty())
            {
                description = desc.text();
                break;
            }
        }
        
        
        int kcalPer100g = 0;

        /*
            Looking for the kcal value of the product if available, it is stored in a table called nutritionTable.
            Need to look in the table for a row with the text "kcal" to identfiy if the product has a kcal value.
            If it does have a kcal value then select the first <td> cell of that row as the value required is in that cell.
        */
        Elements nutritionTableElements = scraper.findSiteElements(subSiteDocument, "table.nutritionTable");
        Elements rows = nutritionTableElements.select("tr"); // Retrieves all the rows in the tables.

        //Iterate through the rows of the table .(i=1)First row is the col names of the table so skip it.
        for (int i = 1; i < rows.size(); i++)
        {
            // Gets the whole row the table.
            Element row = rows.get(i);
            
            // If the row has the text "kcal" then it is the correct row to scrape the value from.
            if (row.text().toLowerCase().contains("kcal"))
            {
                // Get the first matching <td> cell from the row.
                Element cols = row.selectFirst("td");
                // The value could be alphanumeric e.g. "43kcal" , need to remove the text & just get the value 42.
                kcalPer100g = FormatterUtils.cleanseAlphaNumToInt(cols.text());
                break;
            }
        }

        // Update the productfile model with scraped values.
        prodFile.setUnitPrice(unitPrice);
        prodFile.setDescription(description);
        prodFile.setKcalPer100g(kcalPer100g);

        System.out.println("    Price : " + unitPrice + " Description : " + description + " kcal: " + kcalPer100g);

    }

    public List<ProductFile> getProductFileList()
    {
        return productFileList;
    }

    public void setProductFileList(List<ProductFile> productFileList)
    {
        this.productFileList = productFileList;
    }

    public SiteScraper getScraper()
    {
        return scraper;
    }

    public void setScraper(SiteScraper scraper)
    {
        this.scraper = scraper;
    }
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        /*
            Issuse running scraper in java 1.7
            Error - javax.net.ssl.SSLException: Received fatal alert: protocol_version
        
            Fix Found - https://stackoverflow.com/questions/16541627/javax-net-ssl-sslexception-received-fatal-alert-protocol-version
            Looks like Sainsburys site is using a higher tls than Java1.7 default so needs adding into the allowed protcols.
        */
        //
        java.lang.System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        SainsburysScraper ss = new SainsburysScraper();
    }

}
