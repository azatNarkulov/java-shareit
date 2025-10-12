package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, Long userId);

    ItemDto updateItem(Long itemId, Long userId, ItemDto itemDto);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getItemsByUserId(Long userId);

    List<ItemDto> searchItems(String text, Long userId);
}
