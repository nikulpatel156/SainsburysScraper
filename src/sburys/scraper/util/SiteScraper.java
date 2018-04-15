/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sburys.scraper.util;

import java.io.IOException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author NIKUL PATEL
 */
public interface SiteScraper
{
    
    public Document getSiteAsDocument(String url)throws IOException;
    
    public Elements findSiteElements(Document document , String tag);
    
    public Element findSiteElement(Document document , String tag);
    
}
