package com.pragma.plazoletaservice.infraestructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.plazoletaservice.application.dto.request.DishRequestDto;
import com.pragma.plazoletaservice.application.dto.response.DishResponseDto;
import com.pragma.plazoletaservice.application.handler.IDishHandler;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DishRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IDishHandler dishHandler;

    @InjectMocks
    private DishRestController dishRestController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private DishRequestDto dishRequestDto;
    private DishResponseDto dishResponseDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dishRestController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        // Configurar DishRequestDto con todos sus atributos
        dishRequestDto = new DishRequestDto();
        dishRequestDto.setName("Plato de prueba");
        dishRequestDto.setDescription("Descripción del plato");
        dishRequestDto.setPrice(15000L);
        dishRequestDto.setUrlImage("http://example.com/imagen.jpg");
        dishRequestDto.setCategoryId(1L);
        dishRequestDto.setRestaurantId(1L);
        dishRequestDto.setActive(true);

        // Configurar DishResponseDto con todos sus atributos
        dishResponseDto = new DishResponseDto();
        dishResponseDto.setId(1L);
        dishResponseDto.setName("Plato de prueba");
        dishResponseDto.setDescription("Descripción del plato");
        dishResponseDto.setPrice(15000L);
        dishResponseDto.setUrlImage("http://example.com/imagen.jpg");
        dishResponseDto.setActive(true);
        dishResponseDto.setCategoryId(1L);
        dishResponseDto.setRestaurantId(1L);
    }

    @Test
    void saveDish() throws Exception {
        when(dishHandler.saveDish(any(DishRequestDto.class))).thenReturn(dishResponseDto);

        mockMvc.perform(post("/api/v1/dishes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishRequestDto)))
                .andExpect(status().isOk());

        verify(dishHandler).saveDish(any(DishRequestDto.class));
    }

    @Test
    void updateDish() throws Exception {
        when(dishHandler.updateDish(anyLong(), any(DishRequestDto.class))).thenReturn(dishResponseDto);

        mockMvc.perform(put("/api/v1/dishes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Plato de prueba"))
                .andExpect(jsonPath("$.description").value("Descripción del plato"))
                .andExpect(jsonPath("$.price").value(15000))
                .andExpect(jsonPath("$.urlImage").value("http://example.com/imagen.jpg"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.restaurantId").value(1L));

        verify(dishHandler).updateDish(eq(1L), any(DishRequestDto.class));
    }

    @Test
    void enableDisableDish() throws Exception {
        when(dishHandler.updateDishStatus(anyLong(), anyBoolean())).thenReturn(dishResponseDto);

        mockMvc.perform(patch("/api/v1/dishes/{id}/status", 1L)
                        .param("active", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Plato de prueba"))
                .andExpect(jsonPath("$.description").value("Descripción del plato"))
                .andExpect(jsonPath("$.price").value(15000))
                .andExpect(jsonPath("$.urlImage").value("http://example.com/imagen.jpg"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.restaurantId").value(1L));

        verify(dishHandler).updateDishStatus(eq(1L), eq(true));
    }

    @Test
    void findDishById() throws Exception {
        when(dishHandler.findDishById(anyLong())).thenReturn(dishResponseDto);

        mockMvc.perform(get("/api/v1/dishes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Plato de prueba"))
                .andExpect(jsonPath("$.description").value("Descripción del plato"))
                .andExpect(jsonPath("$.price").value(15000))
                .andExpect(jsonPath("$.urlImage").value("http://example.com/imagen.jpg"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.restaurantId").value(1L));

        verify(dishHandler).findDishById(eq(1L));
    }

    @Test
    void findAllDishesByRestaurantIdAndCategoryId() throws Exception {
        List<DishResponseDto> dishes = Arrays.asList(dishResponseDto);
        when(dishHandler.findAllDishesByRestaurantIdAndCategoryId(anyLong(), anyLong(), any(Pageable.class))).thenReturn(dishes);

        mockMvc.perform(get("/api/v1/dishes/restaurant/{restaurantId}/{categoryId}", 1L, 1L)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Plato de prueba"))
                .andExpect(jsonPath("$[0].description").value("Descripción del plato"))
                .andExpect(jsonPath("$[0].price").value(15000))
                .andExpect(jsonPath("$[0].urlImage").value("http://example.com/imagen.jpg"))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[0].categoryId").value(1L))
                .andExpect(jsonPath("$[0].restaurantId").value(1L));

        verify(dishHandler).findAllDishesByRestaurantIdAndCategoryId(eq(1L), eq(1L), any(Pageable.class));
    }

    @Test
    void findAllDishesByRestaurantId() throws Exception {
        List<DishResponseDto> dishes = Arrays.asList(dishResponseDto);
        when(dishHandler.findAllDishesByRestaurantId(anyLong(), any(Pageable.class))).thenReturn(dishes);

        mockMvc.perform(get("/api/v1/dishes/restaurant/{restaurantId}", 1L)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Plato de prueba"))
                .andExpect(jsonPath("$[0].description").value("Descripción del plato"))
                .andExpect(jsonPath("$[0].price").value(15000))
                .andExpect(jsonPath("$[0].urlImage").value("http://example.com/imagen.jpg"))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[0].categoryId").value(1L))
                .andExpect(jsonPath("$[0].restaurantId").value(1L));

        verify(dishHandler).findAllDishesByRestaurantId(eq(1L), any(Pageable.class));
    }

}