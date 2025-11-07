package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */

@Data
@RequiredArgsConstructor
public class ItemDto {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;
    private Long request; // мб нужно убрать
}
