package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StateStatus;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto createBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody @Valid BookingRequestDto bookingRequestDto
    ) {
        return bookingService.createBooking(bookingRequestDto, userId); // добавление бронирования
    }

    @PatchMapping("{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponseDto updateBookingStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved
    ) {
        return bookingService.updateBookingStatus(bookingId, userId, approved); // подтверждение/отклонение запроса
    }

    @GetMapping("{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponseDto getBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId
    ) {
        return bookingService.getBookingById(bookingId, userId); // получение данных о конкретном бронировании
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponseDto> getUserBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state
    ) {
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponseDto> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state
    ) {
        return bookingService.getOwnerBookings(userId, state);
    }
}
