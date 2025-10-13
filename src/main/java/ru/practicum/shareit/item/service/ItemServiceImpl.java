package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto addItem(ItemDto itemDto, Long userId) {
        checkUserExists(userId);
        return itemRepository.addItem(itemDto, userId);
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemUpdateDto itemDto, Long userId) {
        checkUserExists(userId);
        checkOwner(itemId, userId);
        return itemRepository.updateItem(itemId, itemDto);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public List<ItemDto> getItemsByUserId(Long userId) {
        checkUserExists(userId);
        return itemRepository.getItemsByUserId(userId);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return itemRepository.searchItems(text);
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    private void checkOwner(Long itemId, Long userId) {
        if (!itemRepository.checkOwner(itemId, userId)) {
            throw new NotOwnerException("Пользователь не является владельцем предмета");
        }
    }
}
