package com.journey.miles.api.controller;

import com.journey.miles.api.domain.review.Review;
import com.journey.miles.api.domain.review.dto.ReviewRegistrationData;
import com.journey.miles.api.domain.review.dto.ReviewDetailsData;
import com.journey.miles.api.domain.review.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<ReviewRegistrationData> reviewDataJson;
    @Autowired
    private JacksonTester<ReviewDetailsData> reviewDetailsJson;
    @Autowired
    private JacksonTester<Page<ReviewDetailsData>> reviewDetailsListJson;
    @MockBean
    private ReviewService service;

    @Test
    @DisplayName("receive valid request, should return 201 status code and header wih review id")
    void shouldCreateReviewFromPostRequestWithValidJsonBody() throws Exception {
        //given
        Long id = 1L;
        String name = "Fulano da Silva";
        String reviewText = "this is my option about journey miles";
        String photoPath = "my/photo/path";
        ReviewRegistrationData reviewRegistrationData = new ReviewRegistrationData(name, reviewText, photoPath);
        ReviewDetailsData reviewDetails = new ReviewDetailsData(id, name, reviewText, photoPath);
        when(service.create(reviewRegistrationData)).thenReturn(reviewDetails);

        //when and then
        mockMvc.perform(
                        post("/api/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(reviewDataJson.write(reviewRegistrationData).getJson()))
                .andExpect(status().isCreated())
                .andExpect(content().json(reviewDetailsJson.write(reviewDetails).getJson()))
                .andExpect(header().string("Location", containsString("/review/" + id)));

        verify(service).create(reviewRegistrationData);
    }

    @Test
    @DisplayName("receive invalid request with null name, should return 404 status code")
    void shouldNotCreateReviewFromInvalidRequestWithNullName() throws Exception {
        //given
        Long id = 1L;
        String name = "";
        String reviewText = "this is my option about journey miles";
        String photoPath = "my/photo/path";
        ReviewRegistrationData reviewRegistrationData = new ReviewRegistrationData(name, reviewText, photoPath);
        ReviewDetailsData reviewDetails = new ReviewDetailsData(id, name, reviewText, photoPath);
        when(service.create(reviewRegistrationData)).thenReturn(reviewDetails);

        //when and then
        mockMvc
                .perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewDataJson.write(reviewRegistrationData).getJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("receive invalid request with null description, should return 404 status code")
    void shouldNotCreateReviewFromInvalidRequestWithNullDescription() throws Exception {
        //given
        Long id = 1L;
        String name = "Fulano da Silva";
        String description = "";
        String photoPath = "my/photo/path";
        ReviewRegistrationData reviewRegistrationData = new ReviewRegistrationData(name, description, photoPath);
        ReviewDetailsData reviewDetails = new ReviewDetailsData(id, name, description, photoPath);
        when(service.create(reviewRegistrationData)).thenReturn(reviewDetails);

        //when and then
        mockMvc
                .perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewDataJson.write(reviewRegistrationData).getJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnReviewListWithMultipleElements() throws Exception {
        //given
        String name = "Fulano da Silva";
        String description = "";
        String photoPath = "my/photo/path";
        ReviewRegistrationData reviewRegistrationData = new ReviewRegistrationData(name, description, photoPath);
        List<ReviewDetailsData> reviews = new ArrayList<>();
        reviews.add(new ReviewDetailsData(new Review(reviewRegistrationData)));
        reviews.add(new ReviewDetailsData(new Review(reviewRegistrationData)));

        Pageable pageable = PageRequest.of(0, 10);
        Page<ReviewDetailsData> reviewPage = new PageImpl<>(reviews, pageable, reviews.size());

        when(service.findAll(pageable)).thenReturn(reviewPage);

        //when and then
        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(content().json(reviewDetailsListJson.write(reviewPage).getJson()));
        verify(service).findAll(pageable);
    }

    @Test
    void shouldReturnEmptyReviewList() throws Exception {
        //given
        List<ReviewDetailsData> reviews = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 10);
        Page<ReviewDetailsData> reviewPage = new PageImpl<>(reviews, pageable, reviews.size());
        when(service.findAll(pageable)).thenReturn(reviewPage);

        //when and then
        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(content().json(reviewDetailsListJson.write(reviewPage).getJson()));

        verify(service).findAll(pageable);
    }

    @Test
    void shouldReturnSpecificReviewFromId() throws Exception {
        //given
        Long id = 1L;
        String name = "Fulano da Silva";
        String description = "";
        String photoPath = "my/photo/path";
        ReviewDetailsData detailsData = new ReviewDetailsData(new Review(id, name, description, photoPath));
        when(service.findById(id)).thenReturn(detailsData);

        //when and then
        mockMvc.perform(get("/api/reviews/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json((reviewDetailsJson.write(detailsData).getJson())));
        verify(service).findById(id);
    }

    @Test
    void shouldReturnNotFoundWhenSearchByIdThatDontExist() throws Exception {
        //given
        Long id = 1L;
        when(service.findById(id)).thenThrow(EntityNotFoundException.class);

        //when and then
        mockMvc.perform(get("/api/reviews/" + id))
                .andExpect(status().isNotFound());
        verify(service).findById(id);
    }

    @Test
    void shouldReturnUpdatedReviewDetailsDataWhenReceiveValidUpdateRequest() throws Exception {
        //given
        Long id = 1L;
        String name = "Fulano da Silva";
        String description = "";
        String photoPath = "my/photo/path";
        ReviewDetailsData detailsData = new ReviewDetailsData(id, name, description, photoPath);
        String json = reviewDetailsJson.write(detailsData).getJson();
        when(service.update(any())).thenReturn(detailsData);

        //when and then
        mockMvc.perform(put("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void shouldReturnBadRequestWhenRecieveUpdateRequestWithoutId() throws Exception {
        //given
        Long id = null;
        String name = "Fulano da Silva";
        String description = "";
        String photoPath = "my/photo/path";
        ReviewDetailsData detailsData = new ReviewDetailsData(id, name, description, photoPath);
        String json = reviewDetailsJson.write(detailsData).getJson();
        when(service.update(any())).thenReturn(detailsData);

        //when and then
        mockMvc.perform(put("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNoContentWhenDeleteExistingReview() throws Exception {
        //given
        Long id = 1L;

        //when and then
        mockMvc.perform(delete("/api/reviews/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeleteNonexistentReview() throws Exception {
        //given
        Long id = 1L;
        doThrow(EntityNotFoundException.class).when(service).delete(any());

        //when and then
        mockMvc.perform(delete("/api/reviews/" + id))
                .andExpect(status().isNotFound());
    }

}