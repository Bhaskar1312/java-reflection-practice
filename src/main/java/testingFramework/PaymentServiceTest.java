package testingFramework;

import java.util.*;
import java.lang.reflect.*;

public class PaymentServiceTest {

    private PaymentService service;

    public PaymentServiceTest() {

    }

    public static void beforeClass() {
        System.out.println("before class");
    }

    public void setupTest() {
        System.out.println("set up test");
    }

    public void testCreditCardPayment() {
        System.out.println("credit card pay test");
    }

    public void testWireTransfer() {
        System.out.println("test wire transfer");
    }

    public void testInsufficientFunds() {
        System.out.println("insufficient fund test");
    }

    public static void afterClass() {
        System.out.println("after class method");
    }
}