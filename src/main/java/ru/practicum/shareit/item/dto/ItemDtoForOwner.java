package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.LastNextBooking;

import java.util.List;

@Data
@RequiredArgsConstructor
//@NoArgsConstructor
public class ItemDtoForOwner {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private LastNextBooking lastBooking;
    private LastNextBooking nextBooking;
    private List<CommentDto> comments;

    public ItemDtoForOwner(Long id, String name, String description, Boolean available) {
    }
}
