/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sburys.scraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.nodes.Document;
import sburys.model.ProductFile;
import sburys.scraper.util.SiteScraper;
import sburys.scraper.util.impl.SiteScraperImpl;

/**
 *
 * @author NIKUL PATEL
 */
public class SainsburysScraper
{
    
    private final String MAIN_SITE_URL = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
    
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
        
        System.out.println(mainSiteDocument.hasText());
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        SainsburysScraper ss = new SainsburysScraper();
    }
    
}
