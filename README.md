# SainsburysScraper

Java application to scrape the Sainsburys website.

Project Details  : https://jsainsburyplc.github.io/serverside-test/




Run Instructions
----------------


 
Download the "SainsburysScraper.jar" file locally from the /release folder   https://github.com/nikulpatel156/SainsburysScraper/tree/SainsburysScraper/release.
Navigate the command line to the folder with the downloaded jar.
 
Type the following in the command line to run the application:
java -jar "SainsburysScraper.jar" 
 
 

Application Instructions
---------------- 

In the console a menu will be presented. Follow the on screen options to run the application. 


Improvements
----------------




Improvements :

The solution works but given more time the following improvements can be made :

Split up the SainsburysScraper to handle different sections e.g. a method to get the product Title ,method to get the unit price.
In the future if the process to change getting the product title changes it would mean just changing the code only in the related method.

Move the HTML look up tags to a separate class or a properties file so if they change it can be updated from one place.	

In the SainsburysScraper move the HTML tags to variables/properties files so they can be changed easily or create methods to take in the tags to allow methods to be reused.

Add methods in the SiteScraper to handle extracting text values from the element(s) it can also check for null values. 
E.g. "productElement.selectFirst("div.productInfo div.productNameAndPromotions").text()" currently does not check for null or empty values.
This needs adding in to check for nulls or a method in the site scraper to handle the process.

Allow the console to take in a site URL so a new site can be scraped or move the URL to a properties file.

In the ProductFile model remove the toJSONObj() , move it to a separate class to handle the converting. This can then be updated in the future to allow for the product data to be extracted to different formats (csv,xml). Also makes it easier to be extended for different solutions e.g. an another system may want the kcal value even if its 0.

The URL could also do with being validated first before it is used.

The looking up of the kcal value needs to be more robust, it’s reliant upon the row containing the text "kcal". Checks could be to scan the table & check the value is based on per "100g" & nothing else. The kcal value doesn't have a guaranteed index in the nutritionTable so it should can the whole table looking for the value & if it is possible 2 or matches are found there should be some sort of priority to determine which is more accurate.

Looking up the product description is reliant on the description being in the first div.productText on the page, there are multiple div.productText in the page so as an added check to ensure the correct value for the description is found you need the look for a h3 with the value "Description" if the dev.productText is after this then it is more likely to be the description.

Not sure what the extended use of the application is , maybe to scrape the website then validate the site information is displaying correctly to what is in the main Sainsburys product database to ensure pricing , kcal details, etc are correct. For this it would be better to store the retrieved data in a database.

Could add the option to export the extracted data to a file.

A JSON Validator to ensure the output is in a valid format.

Alot more unit testing is required to ensure the data is being scraped accurately. Mock web sites need to be created which can then be used for testing. Frameworks like mockito can be added mock objects for more robust testing.

Use Maven or Ant to automate the build process to build the jar & include any dependencies.


