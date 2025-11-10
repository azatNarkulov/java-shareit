package ru.practicum.shareit.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String error;
}
