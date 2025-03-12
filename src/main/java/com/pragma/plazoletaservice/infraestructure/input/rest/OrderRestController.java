package com.pragma.plazoletaservice.infraestructure.input.rest;

import com.pragma.plazoletaservice.application.dto.request.OrderRequestDto;
import com.pragma.plazoletaservice.application.dto.response.OrderResponseDto;
import com.pragma.plazoletaservice.application.dto.response.RestaurantClientResponseDto;
import com.pragma.plazoletaservice.application.handler.IOrderHandler;
import com.pragma.plazoletaservice.domain.helpers.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderRestController {

    private final IOrderHandler orderHandler;

    @Operation(summary = "Get Orders By State")
    @ApiResponse(responseCode = "200", description = "Orders found")
    @GetMapping("/by-status")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByState( @RequestParam OrderStatus status, Pageable pageable ) {
        return ResponseEntity.ok(orderHandler.getOrdersByState(status, pageable));
    }

    @Operation(summary = "Assign employee to order")
    @ApiResponse(responseCode = "201", description = "Order updated")
    @PatchMapping("/assign-order/{employeeId}/{orderId}")
    public ResponseEntity<OrderResponseDto> assignEmployeeToOrder( @PathVariable Long employeeId, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderHandler.assignEmployeeToOrder(employeeId, orderId));
    }

    @Operation(summary = "Update order status")
    @ApiResponse(responseCode = "201", description = "Order updated")
    @PatchMapping("/update-status/{orderId}/{status}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus( @PathVariable Long orderId, @PathVariable OrderStatus status, @RequestParam(required = false) String code) {
        return ResponseEntity.ok(orderHandler.updateOrderStatus(orderId, status, code));
    }

    @Operation(summary = "Create Order")
    @ApiResponse(responseCode = "201", description = "Order created")
    @PostMapping("/")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.ok(orderHandler.createOrder(orderRequestDto));
    }
}
