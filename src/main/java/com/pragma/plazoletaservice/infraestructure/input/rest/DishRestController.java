package com.pragma.plazoletaservice.infraestructure.input.rest;

import com.pragma.plazoletaservice.application.dto.request.DishRequestDto;
import com.pragma.plazoletaservice.application.dto.response.DishResponseDto;
import com.pragma.plazoletaservice.application.dto.response.RestaurantResponseDto;
import com.pragma.plazoletaservice.application.handler.IDishHandler;
import com.pragma.plazoletaservice.infraestructure.out.jpa.entity.DishEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dishes")
@RequiredArgsConstructor
public class DishRestController {

    private final IDishHandler dishHandler;

    @Operation(summary = "Add a new Dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dish created", content = @Content),
            @ApiResponse(responseCode = "409", description = "Dish already exists", content = @Content)
    })
    @PostMapping("/")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<DishResponseDto> createDish(@RequestBody DishRequestDto dishRequestDto) {
        return ResponseEntity.ok(dishHandler.saveDish(dishRequestDto));
    }

    @Operation(summary = "Get all dishes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All dishes returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DishResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public ResponseEntity<List<DishResponseDto>> getAllDishes() {
        return ResponseEntity.ok(dishHandler.findAllDishes());
    }

    @Operation(summary = "Get dish by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish returned",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DishResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @PostMapping("/{id}")
    public ResponseEntity<DishResponseDto> findDishById(@RequestBody Long id) {
        return ResponseEntity.ok(dishHandler.findDishById(id));
    }

    @Operation(summary = "Update a dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Dish not found", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<DishResponseDto> updateDish(@PathVariable(value = "id")Long dishId,@RequestBody DishRequestDto dishRequestDto) {
        return ResponseEntity.ok(dishHandler.updateDish(dishId, dishRequestDto));
    }

    @Operation(summary = "Enable or disable a dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish status updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Dish not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not authorized to modify this dish", content = @Content)
    })
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<DishResponseDto> updateDishStatus(
            @PathVariable(value = "id") Long dishId,
            @RequestParam(name = "active") Boolean active) {
        return ResponseEntity.ok(dishHandler.updateDishStatus(dishId, active));
    }
}
