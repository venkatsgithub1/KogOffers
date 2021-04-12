package com.kog.spr.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "offers" })
@Builder
public class Location implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 427014039196153249L;

    public Location(Long locationId, String latitude, String longitude) {
        this.locationId = locationId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long locationId;
    private String latitude;
    private String longitude;
    @OneToMany(mappedBy = "locationId")
    @Exclude
    private Set<Offer> offers = new HashSet<>();
}
