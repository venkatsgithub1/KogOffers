package com.kog.spr;

import com.kog.spr.entity.Location;
import com.kog.spr.entity.Offer;
import com.kog.spr.model.OfferSavedMessageVO;
import com.kog.spr.model.OfferWithoutLocationDTO;
import com.kog.spr.service.OffersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PersistentTests {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OffersService offersService;

    HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    void createHeaders() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = new String(Base64.getEncoder().encode(
                ("admin" + ":" + "admin1234").getBytes()));
        headers.set("Authorization", "Basic " + token);
    }

    /*
    This test persists data into DB, since H2 is in memory right now,
    run the test when the application is running.
    Two offers get persisted.
    Run any get offers tests after this test.
     */
    @Test
    @Order(1)
    void testOfferCreation() {
        Offer offerObject = createAnOffer("51.5074° N",
                "0.1278° W", "test100", 3, 5, 31);

        offersService.getImageForOffer(offerObject);

        Offer offerObject2 = createAnOffer("40.7128° N",
                "74.0060° W", "test101", 4, 6, 30);

        offersService.getImageForOffer(offerObject2);

        List<Offer> list = new ArrayList<>();
        list.add(offerObject);
        list.add(offerObject2);

        for (int i = 0; i < 10; i++) {
            Offer offerObjectx = createAnOffer("40.7128° N",
                    "74.0060° W", "test" + (110 + i), 4, 6, 30);
            offersService.getImageForOffer(offerObjectx);
            list.add(offerObjectx);
        }

        list.forEach(offer -> {
            HttpEntity<Offer> httpEntity = new HttpEntity<>(offer, headers);
            ResponseEntity<OfferSavedMessageVO> offerSavedMessageVORespEntity = this.restTemplate
                    .postForEntity("http" +
                            "://localhost:8080/kog/api/collect/offer", httpEntity, OfferSavedMessageVO.class);
            System.out.println(offerSavedMessageVORespEntity + ": res");
            assertNotNull(offerSavedMessageVORespEntity);
            assertNotNull(offerSavedMessageVORespEntity.getBody());
            assertTrue(Objects.requireNonNull(offerSavedMessageVORespEntity.getBody()).getHasSaved());
            assertEquals(200, offerSavedMessageVORespEntity.getStatusCode().value());
        });
    }

    private Offer createAnOffer(String s, String s2, String test100, int i, int i2, int i3) {
        Location locObject = Location.builder()
                .latitude(s)
                .longitude(s2)
                .build();
        return Offer.builder()
                .locationId(locObject)
                .name(test100)
                .validFrom(LocalDate.of(2021, i, 1))
                .validTill(LocalDate.of(2021, i2, i3))
                .imageObjectUrl("")
                .build();
    }

    @Test
    @Order(2)
    void listOffersByPagination() {

        HttpEntity<Offer> httpEntity = new HttpEntity<>(headers);

        // querying for page 0, 1st page with 10 items.
        ResponseEntity<Offer[]> offerRespEntity = this.restTemplate
                .exchange("http" +
                                "://localhost:8080/kog/api/collect/offerByValidFromDescAndPagination?pages=" + 0 + "&" +
                                "pageSize=" + 10,
                        HttpMethod.GET,
                        httpEntity, Offer[].class);

        assertNotNull(offerRespEntity.getBody());
        List<Offer> list = Arrays
                .stream(offerRespEntity.getBody())
                .collect(Collectors.toList());

        // to verify the descending order.
        System.out.println(list);

        // only if previous test is run first and next this.
        assertTrue(list.size() > 1);
    }

    @Test
    @Order(3)
    void listOffersProjectionWithoutLocation() {

        HttpEntity<Offer> httpEntity = new HttpEntity<>(headers);

        // querying for page 0, 1st page with 10 items.
        ResponseEntity<OfferWithoutLocationDTO[]> offerRespEntity = this.restTemplate
                .exchange("http" +
                                "://localhost:8080/kog/api/collect/findAllRecordsWithoutLocationInfo",
                        HttpMethod.GET,
                        httpEntity, OfferWithoutLocationDTO[].class);

        assertNotNull(offerRespEntity.getBody());
        List<OfferWithoutLocationDTO> list = Arrays
                .stream(offerRespEntity.getBody())
                .collect(Collectors.toList());

        // to verify the descending order.
        System.out.println(list);

        // only if previous test is run first and next this.
        assertTrue(list.size() > 1);
    }

}
