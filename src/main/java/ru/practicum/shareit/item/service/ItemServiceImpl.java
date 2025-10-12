package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public ItemDto addItem(ItemDto itemDto, Long userId) {
        return itemRepository.addItem(itemDto, userId);
    }

    @Override
    public ItemDto updateItem(Long itemId, Long userId, ItemDto itemDto) {
        checkUserExists(userId);
        checkOwner(itemId, userId);
        return itemRepository.updateItem(itemId, userId, itemDto);
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        return itemRepository.getItemById(itemId, userId);
    }

    @Override
    public List<ItemDto> getItemsByUserId(Long userId) {
        checkUserExists(userId);
        return itemRepository.getItemsByUserId(userId);
    }

    @Override
    public List<ItemDto> searchItems(String text, Long userId) {
        return itemRepository.searchItems(text, userId);
    }

    private void checkUserExists(Long userId) {
        if (!itemRepository.checkUserExistsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    private void checkOwner(Long itemId, Long userId) {
        if (!itemRepository.checkOwner(itemId, userId)) {
            throw new NotOwnerException("Пользователь не является владельцем предмета");
        }
    }
}
