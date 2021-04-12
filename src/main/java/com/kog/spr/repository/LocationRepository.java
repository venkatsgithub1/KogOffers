package com.kog.spr.repository;

import java.util.Optional;

import com.kog.spr.entity.Location;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Location repository that helps in retrieving data from Location entity.
 */
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findOptionalByLatitudeAndLongitude(String latitude, String longitude);
}
