package com.ardent.epicreads.repository;

import com.ardent.epicreads.entity.Order;
import com.ardent.epicreads.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUser(User user);

    void deleteByUserId(Long userId);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE EXTRACT(MONTH FROM o.orderDate) = EXTRACT(MONTH FROM CURRENT_DATE) AND EXTRACT(YEAR FROM o.orderDate) = EXTRACT(YEAR FROM CURRENT_DATE)")
    double findMonthlyRevenue();

    @Query("SELECT COUNT(o) FROM Order o WHERE EXTRACT(MONTH FROM o.orderDate) = EXTRACT(MONTH FROM CURRENT_DATE) AND EXTRACT(YEAR FROM o.orderDate) = EXTRACT(YEAR FROM CURRENT_DATE)")
    long countMonthlyOrders();

    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    double findTotalRevenue();

    @Query(value = "SELECT * FROM orders ORDER BY order_date DESC LIMIT 7", nativeQuery = true)
    List<Order> findTop5OrdersByOrderDateDesc();
}
