package com.rentspace.listingservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name="listing")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"photos"})
public class Listing {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Double pricePerNight;
    private String location;
    private Long userId;


    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos;
}
