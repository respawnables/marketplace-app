package com.clouify.orderservice.service;

import com.clouify.orderservice.constants.ApplicationConstants;
import com.clouify.orderservice.entity.Order;
import com.clouify.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;

    private final StockServiceClient stockServiceClient;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderService(final OrderRepository orderRepository,
                        final StockServiceClient stockServiceClient,
                        final KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.stockServiceClient = stockServiceClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public Order createOrder(final Order order) {
        final Boolean stockAvailable = stockServiceClient.checkStock(order.getProductId(), order.getAmount());
        log.info("Stock available {} for product id {}", stockAvailable, order.getProductId());
        if (!stockAvailable) {
            throw new RuntimeException("Stock not available for this product: " + order.getProductId());
        }
        stockServiceClient.updateStock(order.getProductId(), order.getAmount());

        final Order saveOrder = orderRepository.save(order);
        kafkaTemplate.send(ApplicationConstants.ORDER_TOPIC, saveOrder.toString());
        return saveOrder;
    }

    @Transactional
    public void cancelOrder(final Long orderId) {
        final Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        stockServiceClient.updateStock(order.getProductId(), order.getAmount());
        orderRepository.delete(order);
        kafkaTemplate.send(ApplicationConstants.ORDER_TOPIC, "Order canceled: " + orderId);
    }

}
