package com.kog.spr.repository;

import java.time.LocalDate;
import java.util.List;

import com.kog.spr.entity.Offer;
import com.kog.spr.model.OfferWithoutLocationDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Offer Repository that is helpful to get information from entities.
 */
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByValidFrom(LocalDate validFrom);

    @Query("SELECT new com.kog.spr.model.OfferWithoutLocationDTO(a.id, a.name, a.validFrom, a.validTill, a.imageObjectUrl) from Offer a")
    List<OfferWithoutLocationDTO> findAllRecords();
}
