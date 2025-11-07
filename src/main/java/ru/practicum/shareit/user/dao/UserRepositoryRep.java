package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;

@Repository
public class UserRepositoryRep {
    private static final HashMap<Long, User> users = new HashMap<>();
    private long counterId = 1;

    public UserDto getUserById(Long id) {
        return UserMapper.toDto(users.get(id));
    }

    public List<UserDto> getUsers() {
        return UserMapper.toDto(users.values());
    }

    public UserDto addUser(UserDto userDto) {
        userDto.setId(counterId++);
        users.put(userDto.getId(), UserMapper.toEntity(userDto));
        return userDto;
    }

    public UserDto updateUser(Long userId, UserUpdateDto newUserDto) {
        User user = users.get(userId);
        if (newUserDto.getName() != null) {
            user.setName(newUserDto.getName());
        }
        if (newUserDto.getEmail() != null) {
            user.setEmail(newUserDto.getEmail());
        }
        return UserMapper.toDto(user);
    }

    public void deleteUser(Long id) {
        users.remove(id);
    }

    public boolean existsByEmail(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    public boolean existsById(Long id) {
        return users.containsKey(id);
    }
}
