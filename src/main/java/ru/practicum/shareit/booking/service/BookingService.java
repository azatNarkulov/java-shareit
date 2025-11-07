package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(BookingRequestDto bookingRequestDto, Long userId);
    BookingResponseDto updateBookingStatus(Long bookingId, Long ownerId, Boolean approved);
    BookingResponseDto getBookingById(Long bookingId, Long userId);
    List<BookingResponseDto> getUserBookings(Long userId, String state);
    List<BookingResponseDto> getOwnerBookings(Long ownerId, String state);
}
