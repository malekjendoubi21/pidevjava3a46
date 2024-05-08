package services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import models.Don;

public class Payment {
    public static void main(String[] args) {

        Stripe.apiKey = "sk_test_51OnL4vFaZiXCQR9PJzhuH3fizwwfYLtFmkkBNC4EyD8tW1GFam8BkEAjGAVpszcAe2fBIuqfa6HwEHYUQkVjqtRR00xvemV4aJ";

        try {

            Account account = Account.retrieve();
            System.out.println("Account ID: " + account.getId());


        } catch (StripeException e) {
            e.printStackTrace();
        }

    }


}