package com.kog.spr;

import com.kog.spr.controller.OfferController;
import com.kog.spr.entity.Location;
import com.kog.spr.entity.Offer;
import com.kog.spr.model.OfferSavedMessageVO;
import com.kog.spr.service.OffersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OfferController.class)
@WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
public class OffersWebMockTest {
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private OffersService offersService;

        @Test
        void createOffersTest() throws Exception {

                String jsonToCreate = "{\"name\":\"test2\",\"validFrom\":\"2021-01-01\",\"validTill\":\"2022-05-31\","
                                + "\"locationId\":{\"latitude\":\"51.5074\u00b0 N\",\"longitude\":\"0.1278\u00b0 W\"},"
                                + "\"imageObjectUrl\":\"\"}";

                OfferSavedMessageVO anyOffer = new OfferSavedMessageVO(true, "");

                when(offersService.saveOffer(any(Offer.class))).thenReturn(anyOffer);

                RequestBuilder requestBuilder = post("/kog/api/collect/offer").accept(MediaType.APPLICATION_JSON)
                                .content(jsonToCreate).contentType(MediaType.APPLICATION_JSON);

                ResultActions resultActions = this.mockMvc.perform(requestBuilder);

                MvcResult result = resultActions.andReturn();
                // checking status of the post request via junit5.
                assertEquals(200, result.getResponse().getStatus());
                assertTrue(result.getResponse().getContentAsString().contains("\"hasSaved\":true"));

                // checking via mockito.
                resultActions.andDo(print()).andExpect(status().isOk());
        }

        @Test
        void getOffers() throws Exception {
                // build mock data.
                List<Offer> mockResults = List.of(new Offer(1L, "test", LocalDate.of(2021, 3, 1),
                                LocalDate.of(2021, 5, 31), new Location(1L, "51.5074° N", "0.1278° W"), ""));

                when(offersService.getAllOffers()).thenReturn(mockResults);

                RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/kog/api/collect/offer")
                                .accept(MediaType.APPLICATION_JSON);
                ResultActions resultActions = this.mockMvc.perform(requestBuilder);

                MvcResult result = resultActions.andReturn();
                // checking status of the get request via junit5 api.
                assertEquals(200, result.getResponse().getStatus());

                // checking results via mockito api.
                resultActions.andDo(print()).andExpect(status().isOk());

        }
}
