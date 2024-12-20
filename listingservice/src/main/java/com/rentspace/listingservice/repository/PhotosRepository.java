package com.rentspace.listingservice.repository;

import com.rentspace.listingservice.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotosRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByListings_Id(Long id);
    void deleteByListings_Id(Long id);
}
