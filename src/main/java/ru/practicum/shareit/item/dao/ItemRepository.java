package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;

@Repository
public class ItemRepository {
    private static final HashMap<Long, Item> items = new HashMap<>();
    private long counterId = 1;

    public ItemDto addItem(ItemDto itemDto, Long userId) {
        itemDto.setId(counterId++);
        Item item = ItemMapper.toItem(itemDto, userId);
        items.put(item.getId(), item);
        return itemDto;
    }

    public ItemDto updateItem(Long itemId, ItemUpdateDto itemDto) {
        Item item = items.get(itemId);
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        item.setAvailable(itemDto.getAvailable());
        return ItemMapper.toItemDto(item);
    }

    public ItemDto getItemById(Long itemId) {
        return ItemMapper.toItemDto(items.get(itemId));
    }

    public List<ItemDto> getItemsByUserId(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(userId))
                .map(ItemMapper::toItemDto)
                .toList();
    }

    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        String searchText = text.toLowerCase();
        return items.values().stream()
                .filter(item -> item.getAvailable() != null && item.getAvailable())
                .filter(item -> (item.getName() != null && item.getName().toLowerCase().contains(searchText)) ||
                        (item.getDescription() != null && item.getDescription().toLowerCase().contains(searchText)))
                .map(ItemMapper::toItemDto)
                .toList();
    }

    public boolean checkOwner(Long itemId, Long userId) {
        Item item = items.get(itemId);
        return item.getOwner().equals(userId);
    }
}
