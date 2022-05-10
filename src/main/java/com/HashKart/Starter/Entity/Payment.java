package com.HashKart.Starter.Entity;

import javax.persistence.GeneratedValue;

public class Payment {

    @GeneratedValue
    private int paymentId;
    private String paymentStatus;
    private String txId;
    private int orderId;
    private double totalAmount;

    private double totalDiscount;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalDiscount() { return totalDiscount;}

    public void setTotalDiscount(double totalDiscount) { this.totalDiscount = totalDiscount;}

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }
}
