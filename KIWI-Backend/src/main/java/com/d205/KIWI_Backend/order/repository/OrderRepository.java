package com.d205.KIWI_Backend.order.repository;

import com.d205.KIWI_Backend.order.domain.Order;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN o.kioskOrders ko WHERE ko.kiosk.id = :kioskId AND o.orderDate > :oneMonthAgo")
    List<Order> findByKioskIdAndOrderDateAfter(@Param("kioskId") Integer kioskId, @Param("oneMonthAgo") LocalDateTime oneMonthAgo);

    @Query("SELECT o FROM Order o JOIN o.kioskOrders ko WHERE ko.kiosk.id = :kioskId AND o.orderDate BETWEEN :startOfMonth AND :endOfMonth")
    List<Order> findByKioskIdAndOrderDateBetween(@Param("kioskId") Integer kioskId, @Param("startOfMonth") LocalDateTime startOfMonth, @Param("endOfMonth") LocalDateTime endOfMonth);

    List<Order> findByOrderDateAfter(LocalDateTime date);

    @Transactional
    @Query(value = "SELECT o.status FROM ORDERS o " +
        "JOIN KIOSK_ORDER k ON o.id = k.ORDER_ID " +
        "WHERE k.KIOSK_ID = :kioskId " +
        "ORDER BY o.ORDER_DATE DESC LIMIT 1", nativeQuery = true)
    String findLatestStatusByKioskId(@Param("kioskId") Long kioskId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE ORDERS o " +
        "JOIN (SELECT order_id FROM KIOSK_ORDER WHERE kiosk_id = :kioskId ORDER BY order_id DESC LIMIT 1) latest_order " +
        "ON o.id = latest_order.order_id " +
        "SET o.status = 'COMPLETED'", nativeQuery = true)
    int updateOrderStatusToCompleted(@Param("kioskId") Long kioskId);

    @Transactional
    @Query(value = "SELECT o.id FROM orders o " +
        "JOIN kiosk_order ko ON o.id = ko.order_id " +
        "WHERE ko.kiosk_id = :kioskId " +
        "ORDER BY o.order_date DESC LIMIT 1", nativeQuery = true)
    Long findLatestOrderIdByKioskId(@Param("kioskId") Long kioskId);

}
