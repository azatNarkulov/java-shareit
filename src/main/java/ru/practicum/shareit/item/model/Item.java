package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.request.model.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */

@Data
@EqualsAndHashCode(of = {"id"})
public class Item {
    private Long id;
    private String name;
    private String description;
    private boolean available;
    private Long owner; // id владельца
    private Long request;

    public Item(Long id, String name, String description, boolean available, Long owner, Long request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
    }
}
