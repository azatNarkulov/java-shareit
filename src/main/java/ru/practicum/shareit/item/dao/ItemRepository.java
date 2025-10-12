package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
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

    public ItemDto updateItem(Long itemId, Long userId, ItemDto itemDto) {
        Item item = items.get(itemId);
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (item.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        item.setAvailable(itemDto.isAvailable());
        return itemDto;
    }

    public ItemDto getItemById(Long itemId, Long userId) {
        return ItemMapper.toItemDto(items.get(itemId));
    }

    public List<ItemDto> getItemsByUserId(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(userId))
                .map(ItemMapper::toItemDto)
                .toList();
    }

    public List<ItemDto> searchItems(String text, Long userId) {
        return items.values().stream()
                .filter(item -> item.isAvailable())
                .filter(item -> item.getDescription().toLowerCase().contains(text))
                .map(ItemMapper::toItemDto)
                .toList();
    }

    public boolean checkUserExistsById(Long userId) {
        return items.values().stream()
                .anyMatch(item -> item.getOwner().equals(userId));
    }

    public boolean checkOwner(Long itemId, Long userId) {
        Item item = items.get(itemId);
        return item.getOwner().equals(userId);
    }
}
