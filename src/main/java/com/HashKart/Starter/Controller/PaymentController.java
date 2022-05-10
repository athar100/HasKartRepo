package com.HashKart.Starter.Controller;

import com.HashKart.Starter.Entity.Payment;
import com.HashKart.Starter.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping("/payment/")
public class PaymentController {

    private static final Logger logger = Logger.getLogger(String.valueOf(PaymentController.class));;
    @Autowired
    public PaymentService service;

    @PostMapping("/doPay/")
    public Payment doPayment(@RequestBody Payment payment){
        logger.log(Level.parse("Payment Object is "), (Supplier<String>) payment);
        return service.doPay(payment);
    }
}