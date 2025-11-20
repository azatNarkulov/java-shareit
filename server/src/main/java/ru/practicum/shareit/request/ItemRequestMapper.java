package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;

import java.util.List;

@Component
public class ItemRequestMapper {
    public ItemRequest toEntity(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());

        return itemRequest;
    }

    public ItemRequestDto toDto(ItemRequest itemRequest) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setCreated(itemRequest.getCreated());

        return dto;
    }

    public List<ItemRequestDto> toDto(List<ItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(this::toDto)
                .toList();
    }

    public ItemShortResponseDto toShortDto(Item item) {
        ItemShortResponseDto dto = new ItemShortResponseDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setOwnerId(item.getOwner().getId());

        return dto;
    }

    public List<ItemShortResponseDto> toShortDto(List<Item> items) {
        return items.stream()
                .map(this::toShortDto)
                .toList();
    }
}
