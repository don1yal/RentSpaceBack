package com.rentspace.listingservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class PhotosDto {

    @NotNull(message = "Photo ID cannot be null.")
    private Long id;

    @NotBlank(message = "URL cannot be empty.")
    private String url;
}
