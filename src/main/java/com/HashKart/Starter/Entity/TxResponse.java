package com.HashKart.Starter.Entity;

import com.HashKart.Starter.Model.OrderDetailInfo;

public class TxResponse {

    private OrderDetailInfo order;
    private double amount;
    private String txId;
    private String status;

    public OrderDetailInfo getOrder() {
        return order;
    }

    public void setOrder(OrderDetailInfo order) {
        this.order = order;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}