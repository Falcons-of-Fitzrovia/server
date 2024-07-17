package com.fof.server.controller;

import com.fof.server.model.normal.PaymentRequestDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RequestMapping("/api/entrepreneur")
@RestController
@RequiredArgsConstructor
public class PaymentController {

    @Value("sk_test_51NVoO5Lg7SFuaaswUBcihqFdHk3rHTbUBIxigRDbFLUcoeGgpDdkS2WsO6QnocfS2xSW9hpzpfHRCinzMH92r9g100rozlXLlX")
    private String stripeSecretKey;

    @PostMapping("/pay")
    public ResponseEntity<String> handlePayment(@RequestBody PaymentRequestDTO paymentRequest) {
        Stripe.apiKey = stripeSecretKey;
        try {
            // Create a charge using the Stripe API
            Charge charge = Charge.create(
                    new HashMap<String, Object>() {{
                        put("amount", paymentRequest.getAmount());
                        put("currency", "LKR");
                        put("source", paymentRequest.getToken());
                        put("description", "Payment for VentureVerse");
                    }}
            );
            return ResponseEntity.ok("Payment successful!");
        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment failed!");
        }
    }

    //Function for the topup payments. The listing id is passed as a parameter
    @PostMapping("/topup/{id}")
    public ResponseEntity<String> handleTopup(@RequestBody PaymentRequestDTO paymentRequest, @PathVariable Integer id) {
        Stripe.apiKey = stripeSecretKey;
        try {
            // Create a charge using the Stripe API
            Charge charge = Charge.create(
                    new HashMap<String, Object>() {{
                        put("amount", paymentRequest.getAmount());
                        put("currency", "LKR");
                        put("source", paymentRequest.getToken());
                        put("description", "Payment for VentureVerse");
                    }}
            );

            return ResponseEntity.ok("Payment successful!");
        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment failed!");
        }
    }

}