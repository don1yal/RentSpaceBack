package com.rentspace.listingservice.repository;

import com.rentspace.listingservice.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingsRepository extends JpaRepository<Listing, Long> {
    boolean existsById(Long id);
    List<Listing> findByUserId(Long userId);
}
