package com.HashKart.Starter.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    private static Logger logger = LoggerFactory.getLogger(FallbackController.class);

    @GetMapping("/productList")
    public ResponseEntity<String> productListFallback() {
        logger.error("productList service is unavailable!");
        return new ResponseEntity<>("productList service is unavailable!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/buyProduct")
    public ResponseEntity<String> buyProductFallback() {
        logger.error("buyProduct service is unavailable!");
        return new ResponseEntity<>("buyProduct service is unavailable!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/shoppingCart")
    public ResponseEntity<String> shoppingCartFallback() {
        logger.error("shoppingCart service is unavailable!");
        return new ResponseEntity<>("shoppingCart service is unavailable!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/shoppingCartCustomer")
    public ResponseEntity<String> shoppingCartCustomerFallback() {
        logger.error("shoppingCartCustomer service is unavailable!");
        return new ResponseEntity<>("shoppingCartCustomer service is unavailable!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/payment")
    public ResponseEntity<String> paymentFallback() {
        logger.error("payment service is unavailable!");
        return new ResponseEntity<>("Payment service is unavailable!", HttpStatus.NOT_FOUND);
    }
}
