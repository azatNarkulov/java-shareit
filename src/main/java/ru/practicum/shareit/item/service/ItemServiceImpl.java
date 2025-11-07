package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

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
}
