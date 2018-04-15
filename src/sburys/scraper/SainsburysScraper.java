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
 */
public class SainsburysScraper
{

    private final String MAIN_SITE_URL = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";

    private final String HTML_LOOKUP_TAG_PRODUCTS = "div[id*=product] div.productInfo";

    private List<ProductFile> productFileList = new ArrayList<ProductFile>();
    SiteScraper scraper;

    public SainsburysScraper()
    {
        scraper = new SiteScraperImpl();

        Document mainSiteDocument = null;
        try
        {
            mainSiteDocument = scraper.getSiteAsDocument(MAIN_SITE_URL);
        }
        catch (IOException ex)
        {
            Logger.getLogger(SainsburysScraper.class.getName()).log(Level.SEVERE, null, ex);
        }

        Elements productItemsElements = scraper.findSiteElements(mainSiteDocument, HTML_LOOKUP_TAG_PRODUCTS);
        System.out.println("Found: " + productItemsElements.size() + " products.");
        scrapeProductItems(productItemsElements);

    }

    public void scrapeProductItems(Elements productItemsElements)
    {

        for (Element productElement : productItemsElements)
        {
            ProductFile prodFile = new ProductFile();
            String productTitle = productElement.selectFirst("div.productInfo div.productNameAndPromotions").text();
            String productDetailsURL = productElement.select("a[href]").get(0).attr("abs:href");

            prodFile.setTitle(productTitle);
            System.out.println("Product : " + productTitle);
            scrapeProductsAdavancedDetails(productDetailsURL, prodFile);
            
            

        }

    }

    public void scrapeProductsAdavancedDetails(String prodDetailsURL, ProductFile prodFile)
    {
        Document subSiteDocument = null;
        try
        {
            subSiteDocument = scraper.getSiteAsDocument(prodDetailsURL);
        }
        catch (IOException ex)
        {
            Logger.getLogger(SainsburysScraper.class.getName()).log(Level.SEVERE, null, ex);
        }

        //<div class="productSummary"> - Unit price is contained with this tag
        Element produtSummaryElement = scraper.findSiteElement(subSiteDocument, "div.productSummary");

        BigDecimal unitPrice = FormatterUtils.cleanseUnitPrice(produtSummaryElement.selectFirst("p.pricePerUnit").text());
        String description = scraper.findSiteElement(subSiteDocument, "div.productText p").text(); //only get the first p tag to scrape the first line only
        int kcalPer100g = 0;

        Elements nutritionTableElements = scraper.findSiteElements(subSiteDocument, "table.nutritionTable");
        Elements rows = nutritionTableElements.select("tr");

        //first row is the col names of the table so skip it.
        for (int i = 1; i < rows.size(); i++)
        {
            Element row = rows.get(i);
            Element cols = row.selectFirst("td");

            if (row.text().toLowerCase().contains("kcal"))
            {
                kcalPer100g = FormatterUtils.cleanseAlphaNumToInt(cols.text());
                break;
            }
        }

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
