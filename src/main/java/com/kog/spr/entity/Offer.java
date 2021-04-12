package com.kog.spr.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    Assuming the table name is same as class name, no name for @Table.
    Column names, same as variable names.
    Offer Entity class represents Offer table.
*/
@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Offer implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2952845210937984945L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    LocalDate validFrom;
    LocalDate validTill;
    @ManyToOne
    @JoinColumn(name = "OFFER_LOCATION_ID")
    Location locationId;
    String imageObjectUrl;
}
