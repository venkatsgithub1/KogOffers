package com.kog.spr.controller;

import java.time.LocalDate;
import java.util.List;

import com.kog.spr.entity.Offer;
import com.kog.spr.model.OfferSavedMessageVO;
import com.kog.spr.model.OfferWithoutLocationDTO;
import com.kog.spr.service.OffersService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

/*
    Offer controller class that has rest APIs related to Offers.
*/
@RestController
@RequestMapping("/kog/api/collect")
public class OfferController {
    private OffersService offersService;

    public OfferController(OffersService offersService) {
        this.offersService = offersService;
    }

    @ApiOperation(value = "/offer", notes = "API to get all the offers without pagination.")
    @GetMapping("/offer")
    public List<Offer> getAllOffers() {
        return this.offersService.getAllOffers();
    }

    @ApiOperation(value = "/offer", notes = "API to create an offer.")
    @PostMapping("/offer")
    public ResponseEntity<OfferSavedMessageVO> saveOffer(@RequestBody Offer offer) {
        OfferSavedMessageVO vo = this.offersService.saveOffer(offer);
        if (!vo.getHasSaved()) {
            return new ResponseEntity<>(vo, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @ApiOperation(value = "/offerByValidFromDescAndPagination", notes = "API to get offers with pagination in validFrom descending order.")
    @GetMapping("/offerByValidFromDescAndPagination")
    @ResponseBody
    public List<Offer> getPaginatedOffersByValidFromDescending(@RequestParam(name = "pages") int pages,
            @RequestParam(name = "pageSize") int pageSize) {
        return this.offersService.getAllOffersWithPaginationSortedByValidFromDescending(pages, pageSize);
    }

    @ApiOperation(value = "/validFrom", notes = "API to get offers information based on validFrom.")
    @GetMapping("/validFrom")
    @ResponseBody
    public List<Offer> getRecordsValidFrom(@RequestParam(name = "validFrom") LocalDate validFrom) {
        return this.offersService.findAllByValidFrom(validFrom);
    }

    @ApiOperation(value = "/validFrom", notes = "API to get offers information projected without location information.")
    @GetMapping("/findAllRecordsWithoutLocationInfo")
    @ResponseBody
    public List<OfferWithoutLocationDTO> getRecords() {
        return this.offersService.findAllRecordsWithoutLocationInfo();
    }
}
