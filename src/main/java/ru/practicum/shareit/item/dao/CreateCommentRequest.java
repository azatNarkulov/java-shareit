package ru.practicum.shareit.item.dao;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCommentRequest {
//    @NotBlank
    private String text;
}
