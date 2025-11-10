package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dao.CreateCommentRequest;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, Long userId);

    ItemDto updateItem(Long itemId, ItemUpdateDto itemDto, Long userId);

    ItemDto getItemById(Long itemId);

    List<ItemDto> getItemsByUserId(Long userId);

    List<ItemDto> searchItems(String text);

    List<ItemDtoForOwner> getItemsByOwner(Long ownerId);

    CommentDto addComment(Long userId, Long itemId, CreateCommentRequest request);
}
