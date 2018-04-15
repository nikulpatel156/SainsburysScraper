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

    @Override
    public Document getSiteAsDocument(String url) throws IOException
    {

        Document document = null;

        document = Jsoup.connect(url).get();

        return document;

    }

    @Override
    public Elements findSiteElements(Document document, String tag)
    {
        
        Elements elementResults = document.select(tag); 
             
        return elementResults;
    }

    @Override
    public Element findSiteElement(Document document, String tag)
    {

        Element elementResults = document.selectFirst(tag);

        return elementResults;

    }

}
