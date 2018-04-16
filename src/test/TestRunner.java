package test;

 
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author NIKUL PATEL
 */
public class TestRunner
{

    public static void main(String[] args)
    {
        System.out.println("TestRunner");
        Result result = JUnitCore.runClasses(ClassTest.class);
        
        System.out.println("Tests Run: " + result.getRunCount()  + " Time: " + result.getRunTime());
        System.out.println("Tests Successful: " + result.wasSuccessful());
        System.out.println("Tests Failed: " + result.getFailureCount());
        
        System.out.println();
        for (Failure failure : result.getFailures())
        {
            System.out.println("FAILED TEST: " + failure.toString());
        }
    }

}
