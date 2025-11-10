package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;

public final class CommentMapper {
    public static CommentDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        String authorName = "Unknown";

        if (comment.getAuthor() != null) {
            authorName = comment.getAuthor().getName();
        }

        return new CommentDto(
                comment.getId(),
                comment.getText(),
                authorName,
                comment.getCreated()
        );
    }
}
