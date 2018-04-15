/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sburys.scraper;

import java.util.Scanner;

/**
 *
 * @author NIKUL PATEL
 */
public class SainsburysScraperConsole
{
    private SainsburysScraper scraper = null;

    public SainsburysScraperConsole()
    {
        System.out.println("Welcome to Sainsburys Site Scraper");
        System.out.println();
        printMenu();
    }
    
    private void printMenu()
    {
        
        System.out.println("    ---- Menu ----"); 
        System.out.println("Please select from the following options:");
        System.out.println("    1. Scrape Site.");
        System.out.println(); 
        System.out.println("    0. Exit");
        waitForInput();
    }
    
    private void waitForInput()
    {
        String option = getInputFromConsole();
        
        if(option == null)
        {
            System.out.println("INVALID OPTION.");
        }
        
        switch(option)
        {
            case "1":
            runScraper();
            break;
        case "0":
            System.out.println("Goodbye.");
            System.exit(0);
        default: 
            System.out.println("INVALID OPTION, Please try again.");
            printMenu();
            break;
        }
    }

    private String getInputFromConsole()
    {
        Scanner scan = new Scanner(System.in);
        System.out.println();
        System.out.print("Input :");
        String myLine = scan.nextLine();
        
        return myLine;
        
    }
    
    private void runScraper()
    {
        System.out.println("Please wait while the site is scraped...");
        if(this.getScraper() == null)
        {
            this.setScraper(new SainsburysScraper());
        }
        else
        {
            this.getScraper().scrapeSite();
        }
    }

    public SainsburysScraper getScraper()
    {
        return scraper;
    }

    public void setScraper(SainsburysScraper scraper)
    {
        this.scraper = scraper;
    }
    
    

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
        SainsburysScraperConsole ss = new SainsburysScraperConsole();
    }

}
