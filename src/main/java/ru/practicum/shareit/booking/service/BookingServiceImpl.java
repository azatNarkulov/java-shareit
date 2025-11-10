package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto, Long userId) {
        User booker = findUserById(userId);

        Item item = itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        if (!item.getAvailable()) {
            throw new ValidationException("Вещь недоступна");
        }

        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Not found");
        }

        LocalDateTime start = bookingRequestDto.getStart();
        LocalDateTime end = bookingRequestDto.getEnd();

        if (start == null || end == null) {
            throw new ValidationException("Отсутствуют даты начала и окончания");
        }

        if (end.isBefore(start) || end.isEqual(start)) {
            throw new ValidationException("Конец бронирования должен быть после времени старта");
        }

        if (start.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Начало бронирования должно быть в будущем");
        }

        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(
                item.getId(), start, end);

        if (!overlappingBookings.isEmpty()) {
            throw new ValidationException("Вещь уже забронирована");
        }

        Booking booking = BookingMapper.toEntity(bookingRequestDto, item, booker);
        Booking savedBooking = bookingRepository.save(booking);

        return BookingMapper.toDto(savedBooking);
    }

    @Override
    @Transactional
    public BookingResponseDto updateBookingStatus(Long bookingId, Long ownerId, boolean approved) {
        Booking booking = findBookingById(bookingId);

        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new NotOwnerException("Только владелец вещи может изменить её статус");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ValidationException("Статус не может быть изменём");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        Booking updatedBooking = bookingRepository.save(booking);

        return BookingMapper.toDto(updatedBooking);
    }

    @Override
    public BookingResponseDto getBookingById(Long bookingId, Long userId) {
        Booking booking = findBookingById(bookingId);

        boolean isBooker = booking.getBooker().getId().equals(userId);
        boolean isOwner = booking.getItem().getOwner().getId().equals(userId);

        if (!isBooker && !isOwner) {
            throw new NotFoundException("Нет прав на просмотр бронирования"); // дописать здесь
        }

        return BookingMapper.toDto(booking);
    }

    @Override
    public List<BookingResponseDto> getUserBookings(Long userId, String state) {
        findUserById(userId);

        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();

        switch (state.toUpperCase()) {
            case "ALL":
                bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);
                break;
            case "CURRENT":
                bookings = bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
                break;
            case "PAST":
                bookings = bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(userId, now);
                break;
            case "FUTURE":
                bookings = bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(userId, now);
                break;
            case "WAITING":
                bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                break;
            default:
                throw new ValidationException("Неизвестная операция");
        }

        return bookings.stream()
                .map(BookingMapper::toDto)
                .toList();
    }

    @Override
    public List<BookingResponseDto> getOwnerBookings(Long ownerId, String state) {
        findUserById(ownerId);

        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();

        switch (state.toUpperCase()) {
            case "ALL":
                bookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(ownerId);
                break;
            case "CURRENT":
                bookings = bookingRepository.findCurrentlyByItemOwnerIdOrderByStartDesc(ownerId, now);
                break;
            case "PAST":
                bookings = bookingRepository.findPastByItemOwnerIdOrderByStartDesc(ownerId, now);
                break;
            case "FUTURE":
                bookings = bookingRepository.findFutureByItemOwnerIdOrderByStartDesc(ownerId, now);
                break;
            case "WAITING":
                bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
                break;
            default:
                throw new ValidationException("Неизвестная операция");
        }

        return bookings.stream()
                .map(BookingMapper::toDto)
                .toList();
    }

    private Booking findBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
