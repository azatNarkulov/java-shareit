package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */

@Data
public class ItemRequestDto {
    private Long id;
    private String description;
    private LocalDateTime created;

    private List<ItemShortResponseDto> items;
}
