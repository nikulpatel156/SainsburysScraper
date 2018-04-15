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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private List<ProductFile> productFileList = new ArrayList<ProductFile>();
    
    // SiteScraper instance that can be used to scrape the site.
    SiteScraper scraper;

    public SainsburysScraper()
    {
        // Initialize the scrapper
        scraper = new SiteScraperImpl();

        // This holds the main site as a document  - Site will be based on the URL - MAIN_SITE_URL.
        Document mainSiteDocument = null;
        try
        {
            mainSiteDocument = scraper.getSiteAsDocument(MAIN_SITE_URL);
        }
        catch (IOException ex)
        {
            Logger.getLogger(SainsburysScraper.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Scrape the site looking for products , should return a list of product elements
        Elements productItemsElements = scraper.findSiteElements(mainSiteDocument, HTML_LOOKUP_TAG_PRODUCTS);
        System.out.println("Found: " + productItemsElements.size() + " products.");
        
        // Pass the product items to be scraped so each products details can be scraped.
        scrapeProductItems(productItemsElements);

    }

    /*
        @param productItemsElements - List of product elements
    */
    public void scrapeProductItems(Elements productItemsElements)
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
            scrapeProductsAdavancedDetails(productDetailsURL, prodFile);
            
            

        }

    }

    /*
        @param prodDetailsURL - URL of the page where the advanced details of the product can be scraped from.
        @param prodFile - ProductFile that will be used to store the found products data.
    */
    public void scrapeProductsAdavancedDetails(String prodDetailsURL, ProductFile prodFile)
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
        }

        //<div class="productSummary"> - Unit price is contained with this tag
        Element produtSummaryElement = scraper.findSiteElement(subSiteDocument, "div.productSummary");

        /*
            Unit price is within a <p> tag called "pricePerUnit". Findes the first matching element. 
            The price is likely in the format "£2.50/unit" so pass the value to the FormatterUtils.cleanseUnitPrice() to return just the price as a BigDecimal.
        */
        BigDecimal unitPrice = FormatterUtils.cleanseUnitPrice(produtSummaryElement.selectFirst("p.pricePerUnit").text());
        String description = scraper.findSiteElement(subSiteDocument, "div.productText p").text(); //only get the first p tag to scrape the first line only
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        SainsburysScraper ss = new SainsburysScraper();
    }

}
