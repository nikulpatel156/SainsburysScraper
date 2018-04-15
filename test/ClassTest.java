/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.math.BigDecimal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sburys.model.ProductFile;

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
             
    
    public ClassTest()
    {
    }
    
    @Test
    public void testProductFileModel() {
        ProductFile product = new ProductFile(PRODUCT_FILE_TITLE, PRODUCT_FILE_KCAL, PRODUCT_FILE_UNIT_PRICE, PRODUCT_FILE_DESCRIPTION);
  
        assertEquals( PRODUCT_FILE_TITLE, product.getTitle());
        assertEquals(PRODUCT_FILE_KCAL, product.getKcalPer100g());
        assertEquals( PRODUCT_FILE_UNIT_PRICE, product.getUnitPrice());
        assertEquals(PRODUCT_FILE_DESCRIPTION, product.getDescription());
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
