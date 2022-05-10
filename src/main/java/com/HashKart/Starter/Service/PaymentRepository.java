package com.HashKart.Starter.Service;

import com.HashKart.Starter.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {
}
