package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public final class BookingMapper {

    public static BookingResponseDto toDto(Booking booking) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(booking.getId());
        bookingResponseDto.setStart(booking.getStart());
        bookingResponseDto.setEnd(booking.getEnd());
        bookingResponseDto.setStatus(booking.getStatus());

        UserDto bookerDto = new UserDto();
        bookerDto.setId(booking.getBooker().getId());
        bookerDto.setName(booking.getBooker().getName());
        bookerDto.setEmail(booking.getBooker().getEmail());
        bookingResponseDto.setBooker(bookerDto);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(booking.getItem().getId());
        itemDto.setName(booking.getItem().getName());
        itemDto.setDescription(booking.getItem().getDescription());
        itemDto.setAvailable(booking.getItem().getAvailable());
        bookingResponseDto.setItem(itemDto);

        return bookingResponseDto;
    }

    public static Booking toEntity(BookingRequestDto bookingRequestDto, Item item, User booker) {
        Booking booking = new Booking();
        booking.setStart(bookingRequestDto.getStart());
        booking.setEnd(bookingRequestDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        return booking;
    }
}
