package com.kog.spr.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
Class used for projection of Offers without Location Information.
*/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OfferWithoutLocationDTO {
    Long id;
    String name;
    LocalDate validFrom;
    LocalDate validTill;
    String imageObjectUrl;
}
