/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sburys.scraper.util.impl;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sburys.scraper.util.SiteScraper;


/**
 *
 * @author NIKUL PATEL
 */
public class SiteScraperImpl implements SiteScraper
{

    /*
        @param url - Takes in a url, used JSOUP to connect to the site & return it as a document.
        @return document - Document of the site requested.
    */
    @Override
    public Document getSiteAsDocument(String url) throws IOException
    {

        Document document = null;

        document = Jsoup.connect(url).get();

        return document;

    }

    /*
        @param document - takes in document of a site
        @param tag - will look for the html in the document for a site. e.g. "div.productInfo" will return ALL the matching divs named productInfo in the site if available.
    
        @return element - returns a list of all the matching Elements.
    */
    @Override
    public Elements findSiteElements(Document document, String tag)
    {
        
        Elements elementResults = document.select(tag); 
             
        return elementResults;
    }

    /*
        @param document - takes in document of a site
        @param tag - will look for the html in the document for a site. e.g. "div.productInfo" will return the FIRST matching divs named productInfo in the site if available.
    
        @return element - Single Element if a matching tag is found.
    */
    @Override
    public Element findSiteElement(Document document, String tag)
    {

        Element elementResults = document.selectFirst(tag);

        return elementResults;

    }

}
