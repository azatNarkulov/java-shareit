package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.LastNextBooking;
import ru.practicum.shareit.item.model.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Data
@RequiredArgsConstructor
//@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;
    private Long request;
    private List<CommentDto> comments = new ArrayList<>();
    private LastNextBooking lastBooking;
    private LastNextBooking nextBooking;

    public ItemDto(Long id, String name, String description, Boolean available, Long request, List<CommentDto> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
        this.comments = comments;
    }
}
