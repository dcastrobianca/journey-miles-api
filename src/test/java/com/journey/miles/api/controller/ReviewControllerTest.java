package com.journey.miles.api.controller;

import com.journey.miles.api.domain.review.Review;
import com.journey.miles.api.domain.review.dto.ReviewData;
import com.journey.miles.api.domain.review.dto.ReviewDetailsData;
import com.journey.miles.api.domain.review.ReviewService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<ReviewData> reviewDataJson;
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
        ReviewData reviewData = new ReviewData(name, reviewText, photoPath);
        ReviewDetailsData reviewDetails = new ReviewDetailsData(id, name, reviewText, photoPath);
        when(service.create(reviewData)).thenReturn(reviewDetails);

        //when
        MockHttpServletResponse response = mockMvc
                .perform(
                        post("/api/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(reviewDataJson.write(reviewData).getJson()))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(reviewDetailsJson.write(reviewDetails).getJson());
        assertThat(response.getHeader("Location").contains("/review/" + id)).isTrue();
    }

    @Test
    @DisplayName("receive invalid request with null name, should return 404 status code")
    void shouldNotCreateReviewFromInvalidRequestWithNullName() throws Exception {
        //given
        Long id = 1L;
        String name = "";
        String reviewText = "this is my option about journey miles";
        String photoPath = "my/photo/path";
        ReviewData reviewData = new ReviewData(name, reviewText, photoPath);
        ReviewDetailsData reviewDetails = new ReviewDetailsData(id, name, reviewText, photoPath);
        when(service.create(reviewData)).thenReturn(reviewDetails);

        //when
        MockHttpServletResponse response = mockMvc
                .perform(
                        post("/api/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(reviewDataJson.write(reviewData).getJson()))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("receive invalid request with null description, should return 404 status code")
    void shouldNotCreateReviewFromInvalidRequestWithNullDescription() throws Exception {
        //given
        Long id = 1L;
        String name = "Fulano da Silva";
        String description = "";
        String photoPath = "my/photo/path";
        ReviewData reviewData = new ReviewData(name, description, photoPath);
        ReviewDetailsData reviewDetails = new ReviewDetailsData(id, name, description, photoPath);
        when(service.create(reviewData)).thenReturn(reviewDetails);

        //when
        MockHttpServletResponse response = mockMvc
                .perform(
                        post("/api/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(reviewDataJson.write(reviewData).getJson()))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturnReviewListWithMultipleElements() throws Exception {
        //given
        String name = "Fulano da Silva";
        String description = "";
        String photoPath = "my/photo/path";
        ReviewData reviewData = new ReviewData(name, description, photoPath);
        List<ReviewDetailsData> reviews = new ArrayList<>();
        reviews.add(new ReviewDetailsData(new Review(reviewData)));
        reviews.add(new ReviewDetailsData(new Review(reviewData)));

        Pageable pageable = PageRequest.of(0,10);
        Page<ReviewDetailsData> reviewPage = new PageImpl<>(reviews, pageable, reviews.size());

        when(service.findAll(pageable)).thenReturn(reviewPage);

        //when and then
        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(content().json(reviewDetailsListJson.write(reviewPage).getJson()));
    }

    @Test
    void shouldReturnEmptyReviewList() throws Exception {
        //given
        List<ReviewDetailsData> reviews = new ArrayList<>();
        Pageable pageable = PageRequest.of(0,10);
        Page<ReviewDetailsData> reviewPage = new PageImpl<>(reviews, pageable, reviews.size());
        when(service.findAll(pageable)).thenReturn(reviewPage);

        //when and then
        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(content().json(reviewDetailsListJson.write(reviewPage).getJson()));
    }


}