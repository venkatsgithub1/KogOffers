package com.kog.spr.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import com.kog.spr.entity.Location;
import com.kog.spr.entity.Offer;
import com.kog.spr.model.OfferSavedMessageVO;
import com.kog.spr.model.OfferWithoutLocationDTO;
import com.kog.spr.model.PhotoVO;
import com.kog.spr.repository.LocationRepository;
import com.kog.spr.repository.OfferRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service class that acts as intermediary between repository and controller to
 * make any necessary transformations.
 */
@Service
public class OffersService {
    private LocationRepository locationRepository;
    private OfferRepository offerRepository;
    private RestTemplate restTemplate;
    private ThreadLocalRandom threadLocalRandom;

    public OffersService(OfferRepository offerRepository, LocationRepository locationRepository,
            RestTemplate restTemplate, ThreadLocalRandom threadLocalRandom) {
        this.offerRepository = offerRepository;
        this.locationRepository = locationRepository;
        this.restTemplate = restTemplate;
        this.threadLocalRandom = threadLocalRandom;
    }

    public List<Offer> getAllOffers() {
        return this.offerRepository.findAll();
    }

    public OfferSavedMessageVO saveOffer(Offer offer) {

        Location location = offer.getLocationId();

        if (Objects.isNull(location)) {
            return OfferSavedMessageVO.builder().hasSaved(false)
                    .message("Can't store offer without location information.").build();
        }
        if (validateLocationObtained(location)) {
            Optional<Location> checkedOptLocation = this.locationRepository
                    .findOptionalByLatitudeAndLongitude(location.getLatitude(), location.getLongitude());
            if (!checkedOptLocation.isPresent()) {
                location = this.locationRepository.saveAndFlush(location);
            } else {
                location = checkedOptLocation.get();
            }
            offer.setLocationId(location);
        } else {
            return OfferSavedMessageVO.builder().hasSaved(false)
                    .message("Location's latitude or longitude is missing, check and try again!").build();
        }

        try {
            getImageForOffer(offer);
            offer = this.offerRepository.saveAndFlush(offer);
        } catch (Exception e) {
            e.printStackTrace();
            return OfferSavedMessageVO.builder().hasSaved(false)
                    .message("Unexpected exception in the system, try once again!").build();
        }

        return OfferSavedMessageVO.builder().hasSaved(true).message("Offer saved with ID: " + offer.getId()).build();
    }

    public void getImageForOffer(Offer offer) {
        PhotoVO photo = this.restTemplate.getForObject(
                "https://jsonplaceholder.typicode.com/photos/" + threadLocalRandom.nextInt(1, 5001), PhotoVO.class);

        if (Objects.nonNull(photo)) {
            offer.setImageObjectUrl(photo.getUrl());
            return;
        }

        offer.setImageObjectUrl("");
    }

    private boolean validateLocationObtained(Location location) {
        if (Objects.isNull(location.getLongitude()) || Objects.isNull(location.getLatitude())) {
            return false;
        }
        return true;
    }

    public List<Offer> findAllByValidFrom(LocalDate validFrom) {
        return this.offerRepository.findByValidFrom(validFrom);
    }

    public List<Offer> getAllOffersWithPaginationSortedByValidFromDescending(int pages, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pages, pageSize, Sort.by("validFrom").descending());
        return this.offerRepository.findAll(pageRequest).toList();
    }

    public List<OfferWithoutLocationDTO> findAllRecordsWithoutLocationInfo() {
        return this.offerRepository.findAllRecords();
    }
}
