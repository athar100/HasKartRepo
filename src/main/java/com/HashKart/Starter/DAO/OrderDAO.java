package com.HashKart.Starter.DAO;

import com.HashKart.Starter.Entity.*;
import com.HashKart.Starter.Model.*;
import com.HashKart.Starter.Pagination.PaginationResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public class OrderDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ProductDAO productDAO;

    private RestTemplate template;

    private int getMaxOrderNum() {
        String sql = "Select max(o.orderNum) from " + Order.class.getName() + " o ";
        Session session = this.sessionFactory.getCurrentSession();
        Query<Integer> query = session.createQuery(sql, Integer.class);
        Integer value = (Integer) query.getSingleResult();
        if (value == null) {
            return 0;
        }
        return value;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveOrder(CartInfo cartInfo) {
        Session session = this.sessionFactory.getCurrentSession();

        int orderNum = this.getMaxOrderNum() + 1;
        Order order = new Order();

        order.setId(UUID.randomUUID().toString());
        order.setOrderNum(orderNum);
        order.setOrderDate(new Date());
        order.setAmount(cartInfo.getAmountTotal());

        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        order.setCustomerName(customerInfo.getName());
        order.setCustomerEmail(customerInfo.getEmail());
        order.setCustomerPhone(customerInfo.getPhone());
        order.setCustomerAddress(customerInfo.getAddress());

        session.persist(order);

        List<CartLineInfo> lines = cartInfo.getCartLines();

        for (CartLineInfo line : lines) {
            OrderDetail detail = new OrderDetail();
            detail.setId(UUID.randomUUID().toString());
            detail.setOrder(order);
            detail.setAmount(line.getAmount());
            detail.setPrice(line.getProductInfo().getPrice());
            detail.setQuantity(line.getQuantity());

            String code = line.getProductInfo().getCode();
            Product product = this.productDAO.findProduct(code);
            detail.setProduct(product);

            session.persist(detail);
        }

        // Order Number!
        cartInfo.setOrderNum(orderNum);
        // Flush
        session.flush();
    }

    // @page = 1, 2, ...
    public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage) {
        String sql = "Select new " + OrderInfo.class.getName()//
                + "(ord.id, ord.orderDate, ord.orderNum, ord.amount, "
                + " ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone) " + " from "
                + Order.class.getName() + " ord "//
                + " order by ord.orderNum desc";

        Session session = this.sessionFactory.getCurrentSession();
        Query<OrderInfo> query = session.createQuery(sql, OrderInfo.class);
        return new PaginationResult<OrderInfo>(query, page, maxResult, maxNavigationPage);
    }

    public Order findOrder(String orderId) {
        Session session = this.sessionFactory.getCurrentSession();
        return session.find(Order.class, orderId);
    }

    public OrderInfo getOrderInfo(String orderId) {
        Order order = this.findOrder(orderId);
        if (order == null) {
            return null;
        }
        return new OrderInfo(order.getId(), order.getOrderDate(), //
                order.getOrderNum(), order.getAmount(), order.getCustomerName(), //
                order.getCustomerAddress(), order.getCustomerEmail(), order.getCustomerPhone());
    }

    public List<OrderDetailInfo> listOrderDetailInfos(String orderId) {
        String sql = "Select new " + OrderDetailInfo.class.getName() //
                + "(d.id, d.product.code, d.product.name , d.quanity,d.price,d.amount) "//
                + " from " + OrderDetail.class.getName() + " d "//
                + " where d.order.id = :orderId ";

        Session session = this.sessionFactory.getCurrentSession();
        Query<OrderDetailInfo> query = session.createQuery(sql, OrderDetailInfo.class);
        query.setParameter("orderId", orderId);

        return query.getResultList();
    }
    public TxResponse placeOrder(OrderDetailInfo order){
        Payment paymentReq = new Payment();
        double totalAmount, newAmount = 0.0, discount = 0.0;
        paymentReq.setOrderId(order.getId());
        totalAmount = order.getQuanity()*order.getPrice();
        // Straight 10% Discount if orders are more than 5 or total amount is greater than 15000
        if(order.getQuanity() >= 5 || totalAmount >= 15000){
            discount = order.getQuanity()*order.getPrice()/10;
            newAmount = order.getQuanity()*order.getPrice() - discount;
        }
        // Straight 20% Discount if orders are more than 10 or total amount is greater than 30000
        else if(order.getQuanity() >= 10 || totalAmount >= 30000){
            discount = order.getQuanity()*order.getPrice()/20;
            newAmount = order.getQuanity()*order.getPrice() - discount;
        }
        else newAmount = totalAmount;
        paymentReq.setTotalAmount(newAmount);
        paymentReq.setTotalDiscount(discount);
        Payment paymentRes =
                template.postForObject("http://localhost:61115/payment/doPay/",
                        paymentReq, Payment.class);
        TxResponse txResponse = new TxResponse();
        txResponse.setOrder(order);
        txResponse.setStatus(paymentRes.getPaymentStatus());
        txResponse.setAmount(paymentRes.getTotalAmount());
        txResponse.setTxId(paymentRes.getTxId());
        return txResponse;
    }

}