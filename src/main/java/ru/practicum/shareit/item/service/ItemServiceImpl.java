package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.LastNextBooking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.CreateCommentRequest;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto addItem(ItemDto itemDto, Long userId) {
        if (userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }

        Item item = ItemMapper.toEntity(itemDto);
        item.setOwner(userId);
        itemRepository.save(item);
        return ItemMapper.toDto(item);
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long itemId, ItemUpdateDto itemDto, Long userId) {
        Item item = findAndCheckItem(itemId);

        checkUserExists(userId); // проверяем существование пользователя
        checkOwner(item, userId); // проверяем, что пользователь – владелец вещи

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        item.setAvailable(itemDto.getAvailable());
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));
        return ItemMapper.toDto(item);
    }

    @Override
    public List<ItemDto> getItemsByUserId(Long userId) {
        checkUserExists(userId);
        return ItemMapper.toDto(itemRepository.findByOwnerId(userId));
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return ItemMapper.toDto(itemRepository.search(text));
    }

    @Override
    public List<ItemDtoForOwner> getItemsByOwner(Long ownerId) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        List<Item> items = itemRepository.findByOwnerId(ownerId);

        return items.stream()
                .map(this::mapToItemDtoForOwner)
                .toList();
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CreateCommentRequest request) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        boolean hasBooked = commentRepository.existsByBookerAndItemAndEndBefore(
                userId, itemId, LocalDateTime.now()
        );

        if (!hasBooked) {
            throw new ValidationException("Пользователь не может прокомментировать данную вещь");
        }

        Comment comment = new Comment(
                request.getText(),
                item.getId(),
                author.getId(),
                LocalDateTime.now()
        );

        Comment savedComment = commentRepository.save(comment);

        return CommentMapper.toDto(savedComment);
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    private void checkOwner(Item item, Long userId) {
        if (!item.getOwner().equals(userId)) {
            throw new NotOwnerException("Пользователь не является владельцем предмета");
        }
    }

    private Item findAndCheckItem(Long itemId) {
        Optional<Item> itemOpt = itemRepository.findById(itemId);
        if (itemOpt.isEmpty()) {
            throw new NotFoundException("Предмет не найден");
        }
        return itemOpt.get();
    }

    private ItemDtoForOwner mapToItemDtoForOwner(Item item) {
        ItemDtoForOwner itemDtoForOwner = ItemMapper.toDtoForOwner(item);

        List<Booking> bookings = bookingRepository.findByItemIdAndStatusInOrderByStartAsc(
                item.getId(), List.of(BookingStatus.APPROVED)
        );

        LocalDateTime now = LocalDateTime.now();

        Booking lastBooking = bookings.stream()
                .filter(b -> b.getEnd().isBefore(now))
                .reduce((first, second) -> second)
                .orElse(null);

        Booking nextBooking = bookings.stream()
                .filter(b -> b.getStart().isAfter(now))
                .findFirst()
                .orElse(null);

        if (lastBooking != null) {
            itemDtoForOwner.setLastBooking(new LastNextBooking(
                    lastBooking.getId(),
                    lastBooking.getBooker().getId(),
                    lastBooking.getStart(),
                    lastBooking.getEnd()
            ));
        }

        if (nextBooking != null) {
            itemDtoForOwner.setNextBooking(new LastNextBooking(
                    nextBooking.getId(),
                    nextBooking.getBooker().getId(),
                    nextBooking.getStart(),
                    nextBooking.getEnd()
            ));
        }

        return itemDtoForOwner;
    }
}
