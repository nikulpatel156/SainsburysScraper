package test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sburys.model.ProductFile;
import sburys.scraper.util.FormatterUtils;
import sburys.scraper.util.SiteScraper;
import sburys.scraper.util.impl.SiteScraperImpl;

/**
 *
 * @author NIKUL PATEL
 */
public class ClassTest
{
    private final String PRODUCT_FILE_TITLE = "Sainsbury's British Cherry & Strawberry Pack 600g";
    private final int PRODUCT_FILE_KCAL = 55;
    private final BigDecimal PRODUCT_FILE_UNIT_PRICE = new BigDecimal(4);
    private final String PRODUCT_FILE_DESCRIPTION = "British Cherry & Strawberry Mixed Pack";

    private final String JSON_KEY_TITLE = "title";
    private final String JSON_KEY_KCAL = "kcal_per_100g";
    private final String JSON_KEY_UNIT_PRICE = "unit_price";
    private final String JSON_KEY_DESCRIPTION = "description";
    
    public ClassTest()
    {
    }
    
    @Test
    public void testProductFileModel() {
        System.out.println("Test - testProductFileModel()");
        ProductFile product = new ProductFile(PRODUCT_FILE_TITLE, PRODUCT_FILE_KCAL, PRODUCT_FILE_UNIT_PRICE, PRODUCT_FILE_DESCRIPTION);
  
        assertEquals( PRODUCT_FILE_TITLE, product.getTitle());
        assertEquals(PRODUCT_FILE_KCAL, product.getKcalPer100g());
        assertEquals( PRODUCT_FILE_UNIT_PRICE, product.getUnitPrice());
        assertEquals(PRODUCT_FILE_DESCRIPTION, product.getDescription());
    }
    
    @Test
    public void testProductFileModelToJSON() {
        
        System.out.println("Test - testProductFileModelToJSON()");
        ProductFile product = new ProductFile(PRODUCT_FILE_TITLE, PRODUCT_FILE_KCAL, PRODUCT_FILE_UNIT_PRICE, PRODUCT_FILE_DESCRIPTION);
  
        assertEquals( PRODUCT_FILE_TITLE, product.getTitle());
        assertEquals(PRODUCT_FILE_KCAL, product.getKcalPer100g());
        assertEquals( PRODUCT_FILE_UNIT_PRICE, product.getUnitPrice());
        assertEquals(PRODUCT_FILE_DESCRIPTION, product.getDescription());
        JSONObject json = product.toJSONObj();
        
        assertEquals(json.length(), 4);
        assertTrue(json.has(JSON_KEY_TITLE));
        assertTrue(json.has(JSON_KEY_UNIT_PRICE));
        assertTrue(json.has(JSON_KEY_KCAL));
        assertTrue(json.has(JSON_KEY_DESCRIPTION));
        
                
        assertEquals(json.get(JSON_KEY_TITLE), PRODUCT_FILE_TITLE);
        assertEquals(json.get(JSON_KEY_UNIT_PRICE), PRODUCT_FILE_UNIT_PRICE);
        assertEquals(json.get(JSON_KEY_KCAL), PRODUCT_FILE_KCAL);
        assertEquals(json.get(JSON_KEY_DESCRIPTION), PRODUCT_FILE_DESCRIPTION);
                 

        
    }
    
    @Test
    public void testProductFileModelToJSONWithKCAL() {
        
        System.out.println("Test - testProductFileModelToJSONWithKCAL()");
        
        ProductFile product = new ProductFile(PRODUCT_FILE_TITLE, 0, PRODUCT_FILE_UNIT_PRICE, PRODUCT_FILE_DESCRIPTION);
  
        assertEquals( PRODUCT_FILE_TITLE, product.getTitle());
        assertEquals(0, product.getKcalPer100g());
        assertEquals( PRODUCT_FILE_UNIT_PRICE, product.getUnitPrice());
        assertEquals(PRODUCT_FILE_DESCRIPTION, product.getDescription());
        JSONObject json = product.toJSONObj();
        
        assertEquals(json.length(), 3);
        assertTrue(json.has(JSON_KEY_TITLE));
        assertTrue(json.has(JSON_KEY_UNIT_PRICE));
        assertFalse(json.has(JSON_KEY_KCAL));
        assertTrue(json.has(JSON_KEY_DESCRIPTION));
        
                
        assertEquals(json.get(JSON_KEY_TITLE), PRODUCT_FILE_TITLE);
        assertEquals(json.get(JSON_KEY_UNIT_PRICE), PRODUCT_FILE_UNIT_PRICE); 
        assertEquals(json.get(JSON_KEY_DESCRIPTION), PRODUCT_FILE_DESCRIPTION);
          
    }
    
     @Test
    public void testSiteScraperReturnsDocument() {
        
        System.out.println("Test - testSiteScraperReturnsDocument()");
        
        SiteScraper scraper = new SiteScraperImpl();
        Document doc = null;
        
        try
        {
            doc = scraper.getSiteAsDocument("http://www.bbc.co.uk/news");
        }
        catch (IOException ex)
        {
            Logger.getLogger(ClassTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        assertNotNull(doc);
        assertTrue(doc.hasText());
    }
    
       // Test with a invalid URL
        @Test   
       public void testSiteScraperReturnsNullDocument() {
           
           System.out.println("Test - testSiteScraperReturnsNullDocument()");
           
        SiteScraper scraper = new SiteScraperImpl();
        Document doc = null;
        
        try
        {
            doc = scraper.getSiteAsDocument("http://www.bbcbbc.co.uk/news");
        }
        catch (IOException ex)
        {
             
        }
        
        
        assertNull(doc); 
    }
       
       
    @Test
    public void testFormatterUtilCleanseAlphaNumToInt() {
        
        System.out.println("Test - testFormatterUtilCleanseAlphaNumToInt()");
        
        int result  = FormatterUtils.cleanseAlphaNumToInt("some123text");
        assertEquals( 123, result); 
    }   
    
    @Test
    public void testFormatterUtilCleanseUnitPrice() {
        
        System.out.println("Test - testFormatterUtilCleanseUnitPrice()");
        
        BigDecimal result  = FormatterUtils.cleanseUnitPrice("£2.50/unit ");
        assertEquals( new BigDecimal(2.50).setScale(2), result); 
    }  
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @ClassTest. For example:
    //
    // @ClassTest
    // public void hello() {}
}
