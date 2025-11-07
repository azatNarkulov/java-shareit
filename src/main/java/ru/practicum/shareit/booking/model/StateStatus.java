package ru.practicum.shareit.booking.model;

public enum StateStatus {
    ALL, // все
    CURRENT, // текущие
    PAST, // завершённые
    FUTURE, // будущие
    WAITING, // ожидающие подтверждения
    REJECTED // отклонённые
}
