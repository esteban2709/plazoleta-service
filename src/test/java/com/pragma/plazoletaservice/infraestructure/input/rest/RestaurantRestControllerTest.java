package com.pragma.plazoletaservice.infraestructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.plazoletaservice.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoletaservice.application.dto.response.RestaurantClientResponseDto;
import com.pragma.plazoletaservice.application.dto.response.RestaurantResponseDto;
import com.pragma.plazoletaservice.application.handler.IRestaurantHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RestaurantRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IRestaurantHandler restaurantHandler;

    @InjectMocks
    private RestaurantRestController restaurantRestController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private RestaurantRequestDto restaurantRequestDto;
    private RestaurantResponseDto restaurantResponseDto;
    private RestaurantClientResponseDto restaurantClientResponseDto;



    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantRestController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        // Configurar RestaurantClientResponseDto con todos sus atributos
        restaurantClientResponseDto = new RestaurantClientResponseDto();
        restaurantClientResponseDto.setUrlLogo("http://example.com/logo.png");
        restaurantClientResponseDto.setName("Restaurant Name");

        // Configurar RestaurantRequestDto con todos sus atributos
        restaurantRequestDto = new RestaurantRequestDto();
        restaurantRequestDto.setName("Restaurant Name");
        restaurantRequestDto.setAddress("Restaurant Address");
        restaurantRequestDto.setPhone("1234567890");
        restaurantRequestDto.setUrlLogo("http://example.com/logo.png");
        restaurantRequestDto.setNit(123456789L);
        restaurantRequestDto.setOwnerId(1L);

        // Configurar RestaurantResponseDto con todos sus atributos
        restaurantResponseDto = new RestaurantResponseDto();
        restaurantResponseDto.setId(1L);
        restaurantResponseDto.setName("Restaurant Name");
        restaurantResponseDto.setAddress("Restaurant Address");
        restaurantResponseDto.setPhone("1234567890");
        restaurantResponseDto.setUrlLogo("http://example.com/logo.png");
        restaurantResponseDto.setNit(123456789L);
        restaurantResponseDto.setOwnerId(1L);
    }

    @Test
    void saveRestaurant() throws Exception {
        when(restaurantHandler.saveRestaurant(any(RestaurantRequestDto.class))).thenReturn(restaurantResponseDto);

        mockMvc.perform(post("/api/v1/restaurants/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantRequestDto)))
                .andExpect(status().isCreated());

        verify(restaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));
    }

    @Test
    void getAllRestaurants() throws Exception {
        List<RestaurantClientResponseDto> restaurantList = Arrays.asList(restaurantClientResponseDto);
        when(restaurantHandler.findAllRestaurants(any(Pageable.class))).thenReturn(restaurantList);

        mockMvc.perform(get("/api/v1/restaurants/")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Restaurant Name"))
                .andExpect(jsonPath("$[0].urlLogo").value("http://example.com/logo.png"));

        verify(restaurantHandler).findAllRestaurants(any(Pageable.class));
    }

    @Test
    void getRestaurantById() throws Exception {
        when(restaurantHandler.findRestaurantById(anyLong())).thenReturn(restaurantResponseDto);

        mockMvc.perform(get("/api/v1/restaurants/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Restaurant Name"))
                .andExpect(jsonPath("$.address").value("Restaurant Address"))
                .andExpect(jsonPath("$.phone").value("1234567890"))
                .andExpect(jsonPath("$.urlLogo").value("http://example.com/logo.png"))
                .andExpect(jsonPath("$.nit").value("123456789"))
                .andExpect(jsonPath("$.ownerId").value(1L));

        verify(restaurantHandler).findRestaurantById(1L);
    }

}
