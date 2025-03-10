package com.rentspace.listingservice.service.impl;

import com.rentspace.listingservice.dto.ListingAvailabilityDto;
import com.rentspace.listingservice.dto.ListingAvailabilityRequest;
import com.rentspace.listingservice.entity.Listing;
import com.rentspace.listingservice.entity.ListingAvailability;
import com.rentspace.listingservice.exception.ListingNotFoundException;
import com.rentspace.listingservice.mapper.ListingAvailabilityMapper;
import com.rentspace.listingservice.repository.ListingAvailabilityRepository;
import com.rentspace.listingservice.repository.ListingsRepository;
import com.rentspace.listingservice.service.ListingAvailabilityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListingAvailabilityServiceImpl implements ListingAvailabilityService {
    private final ListingAvailabilityRepository availabilityRepository;
    private final ListingsRepository listingsRepository;
    private final ListingAvailabilityMapper mapper;

    @Override
    public List<ListingAvailabilityDto> getAvailabilityByListing(Long listingId) {
        log.info("Getting availability for listingId: {}", listingId);

        List<ListingAvailability> availabilities = availabilityRepository.findByListingId(listingId);
        if (availabilities.isEmpty()) {
            log.warn("No availability found for listingId: {}", listingId);
            throw new ListingNotFoundException("Listing", "listingId", listingId);
        }

        return availabilities.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ListingAvailabilityDto setAvailability(Long listingId, ListingAvailabilityRequest request) {
        log.info("Set availability for listingId={} from {} to {} (available={})",
                listingId, request.getStartDate(), request.getEndDate(), request.isAvailable());


        var listing = getListingById(listingId);

        ListingAvailability availability = createAvailability(
                listing, request.getStartDate(), request.getEndDate(), request.isAvailable());
        availability = availabilityRepository.save(availability);

        return mapper.toDto(availability);
    }
    @Override
    public boolean isAvailable(Long listingId, LocalDate startDate, LocalDate endDate) {
        log.info("Checking availability for listingId={} from {} to {}", listingId, startDate, endDate);
        return !availabilityRepository.existsByListingIdAndAvailableTrueAndStartDateBeforeAndEndDateAfter(
                listingId, endDate, startDate);
    }

    @Override
    public ListingAvailabilityDto bookAvailability(Long listingId, LocalDate startDate, LocalDate endDate) {
        log.info("Booking availability for listingId={} from {} to {}", listingId, startDate, endDate);

        if (!availabilityRepository.existsByListingIdAndAvailableTrueAndStartDateBeforeAndEndDateAfter(
                listingId, startDate, endDate)) {
            throw new IllegalStateException("Listing is not available for the selected dates.");
        }

        var listing = getListingById(listingId);

        ListingAvailability availability = createAvailability(listing, startDate, endDate, false);
        availability = availabilityRepository.save(availability);

        return mapper.toDto(availability);
    }

    @Override
    public void cancelBooking(Long listingId, LocalDate startDate, LocalDate endDate) {
        log.info("Canceling booking for listingId={} from {} to {}", listingId, startDate, endDate);

        int deletedCount = availabilityRepository.deleteByListingIdAndStartDateAndEndDateAndAvailableFalse(
                listingId, startDate, endDate);

        if (deletedCount == 0) {
            throw new IllegalStateException("No booking found to cancel for listingId=" + listingId +
                    " from " + startDate + " to " + endDate);
        }
    }

    private ListingAvailability createAvailability(Listing listing, LocalDate startDate, LocalDate endDate, boolean available) {
        return ListingAvailability.builder()
                .listing(listing)
                .startDate(startDate)
                .endDate(endDate)
                .available(available)
                .build();
    }

    private Listing getListingById(Long listingId) {
        return listingsRepository.findById(listingId).orElseThrow(
                () -> new ListingNotFoundException("Listing", "listingId", listingId));
    }
}
