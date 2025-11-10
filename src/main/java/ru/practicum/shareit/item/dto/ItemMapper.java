package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public final class ItemMapper {

//    public static ItemDto toDto(Item item) {
//        return new ItemDto(
//                item.getId(),
//                item.getName(),
//                item.getDescription(),
//                item.getAvailable(),
//                item.getRequest() != null ? item.getRequest() : null,
//                item.getComments()
////                CommentMapper.toDto(item.getComments())
//        );
//    }

    public static ItemDto toDto(Item item) {
        System.out.println("=== IN MAPPER ===");
        System.out.println("Item comments: " + (item.getComments() != null ? item.getComments().size() : "null"));
        if (item.getComments() != null) {
            item.getComments().forEach(c -> System.out.println("Comment: " + c.getId() + " – " + c.getText()));
        }

        ItemDto itemDto = new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest() : null,
                item.getComments().stream()
                        .map(CommentMapper::toDto)
                        .toList()
        );

        System.out.println("DTO comments: " + (itemDto.getComments() != null ? itemDto.getComments().size() : "null"));
        System.out.println("================");

        return itemDto;
    }

    public static List<ItemDto> toDto(List<Item> items) {
        return items.stream()
                .map(ItemMapper::toDto)
                .toList();
    }

    public static ItemDtoForOwner toDtoForOwner(Item item) {
        return new ItemDtoForOwner(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static Item toEntity(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getRequest()
        );
    }
}
