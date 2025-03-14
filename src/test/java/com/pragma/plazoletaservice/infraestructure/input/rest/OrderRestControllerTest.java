package com.pragma.plazoletaservice.infraestructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.plazoletaservice.application.dto.request.OrderDishRequestDto;
import com.pragma.plazoletaservice.application.dto.request.OrderRequestDto;
import com.pragma.plazoletaservice.application.dto.response.*;
import com.pragma.plazoletaservice.application.handler.IOrderHandler;
import com.pragma.plazoletaservice.domain.helpers.OrderStatus;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IOrderHandler orderHandler;

    @InjectMocks
    private OrderRestController orderRestController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private OrderResponseDto orderResponseDto;
    private OrderRequestDto orderRequestDto;
    private RestaurantResponseDto restaurantResponseDto;
    private List<TraceabilityLogResponseDto> traceabilityLogs;
    private List<EmployeeAverageTimeDto> employeeAverageTimes;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderRestController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();

        // Configurar RestaurantResponseDto
        restaurantResponseDto = new RestaurantResponseDto();
        restaurantResponseDto.setId(2L);
        restaurantResponseDto.setName("Restaurant Name");
        restaurantResponseDto.setAddress("Restaurant Address");
        restaurantResponseDto.setPhone("1234567890");
        restaurantResponseDto.setOwnerId(1L);

        // Configurar OrderResponseDto con todos los atributos
        orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(1L);
        orderResponseDto.setOrderStatus(OrderStatus.PENDING);
        orderResponseDto.setRestaurant(restaurantResponseDto);
        orderResponseDto.setChefId(3L);
        orderResponseDto.setClientId(4L);
        OrderDishRequestDto orderDishRequestDto = new OrderDishRequestDto();
        orderDishRequestDto.setDishId(1L);
        orderDishRequestDto.setQuantity(2L);

        // Configurar OrderRequestDto con todos los atributos
        orderRequestDto = new OrderRequestDto();
        orderRequestDto.setRestaurantId(2L);
        orderRequestDto.setClientId(3L);
        orderRequestDto.setOrderDishList(List.of(orderDishRequestDto));

        // Configurar TraceabilityLogResponseDto
        TraceabilityLogResponseDto logResponseDto = new TraceabilityLogResponseDto();
        logResponseDto.setId("1L");
        logResponseDto.setIdOrder(1L);
        traceabilityLogs = Arrays.asList(logResponseDto);

        // Configurar EmployeeAverageTimeDto
        EmployeeAverageTimeDto averageTimeDto = new EmployeeAverageTimeDto();
        averageTimeDto.setIdEmployee(1L);
        averageTimeDto.setAverageTimeMilliseconds(15.0);
        employeeAverageTimes = Arrays.asList(averageTimeDto);
    }

    @Test
    void getOrdersByState() throws Exception {
        List<OrderResponseDto> orderList = Arrays.asList(orderResponseDto);
        when(orderHandler.getOrdersByState(eq(OrderStatus.PENDING), any(Pageable.class))).thenReturn(orderList);

        mockMvc.perform(get("/api/v1/orders/by-status")
                        .param("status", "PENDING")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].orderStatus").value("PENDING"))
                .andExpect(jsonPath("$[0].restaurant.id").value(restaurantResponseDto.getId()))
                .andExpect(jsonPath("$[0].chefId").value(3L))
                .andExpect(jsonPath("$[0].clientId").value(4L));
    }

    @Test
    void assignEmployeeToOrder() throws Exception {
        when(orderHandler.assignEmployeeToOrder(eq(1L), eq(1L))).thenReturn(orderResponseDto);

        mockMvc.perform(patch("/api/v1/orders/assign-order/1/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderStatus").value("PENDING"))
                .andExpect(jsonPath("$.restaurant.id").value(restaurantResponseDto.getId()))
                .andExpect(jsonPath("$.chefId").value(3L))
                .andExpect(jsonPath("$.clientId").value(4L));
    }

    @Test
    void updateOrderStatus() throws Exception {
        when(orderHandler.updateOrderStatus(eq(1L), eq(OrderStatus.PREPARING), anyString())).thenReturn(orderResponseDto);

        mockMvc.perform(patch("/api/v1/orders/update-status/1/PREPARING")
                        .param("code", "ABC123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderStatus").value("PENDING"))
                .andExpect(jsonPath("$.restaurant.id").value(restaurantResponseDto.getId()))
                .andExpect(jsonPath("$.chefId").value(3L))
                .andExpect(jsonPath("$.clientId").value(4L));
    }

    @Test
    void createOrder() throws Exception {
        when(orderHandler.createOrder(any(OrderRequestDto.class))).thenReturn(orderResponseDto);

        mockMvc.perform(post("/api/v1/orders/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderStatus").value("PENDING"))
                .andExpect(jsonPath("$.restaurant.id").value(restaurantResponseDto.getId()))
                .andExpect(jsonPath("$.chefId").value(3L))
                .andExpect(jsonPath("$.clientId").value(4L));

    }

    @Test
    void getOrdersLogsHistory() throws Exception {
        when(orderHandler.getOrdersLogsHistory(anyLong())).thenReturn(traceabilityLogs);

        mockMvc.perform(get("/api/v1/orders/logs-orders-history/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1L"))
                .andExpect(jsonPath("$[0].idOrder").value(1L));
    }

    @Test
    void getEmployeeOrderAverageDurations() throws Exception {
        when(orderHandler.getEmployeeOrderAverageDurations()).thenReturn(employeeAverageTimes);

        mockMvc.perform(get("/api/v1/orders/logs-order-durations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEmployee").value(1L))
                .andExpect(jsonPath("$[0].averageTimeMilliseconds").value(15.0));
    }

}