package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@AllArgsConstructor
public class BookingRequestDto {

    @NotBlank
    private Long itemId;

    @FutureOrPresent
    @NotBlank
    private LocalDateTime start;

    @FutureOrPresent
    @NotBlank
    private LocalDateTime end;
}
