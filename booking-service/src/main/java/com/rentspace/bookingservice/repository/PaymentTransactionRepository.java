package com.rentspace.bookingservice.repository;

import com.rentspace.bookingservice.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    List<PaymentTransaction> findByUserId(Long userId);
    Optional<PaymentTransaction> findByBookingId(Long bookingId);
}
