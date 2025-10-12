package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepository.getUsers();
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        String email = userDto.getEmail();
        checkEmailExists(email);
        return userRepository.addUser(userDto);
    }

    @Override
    public UserDto updateUser(Long userId, UserUpdateDto newUserDto) {
        checkUserExists(userId);
        UserDto currentUser = getUserById(userId);
        if (newUserDto.getEmail() != null && !newUserDto.getEmail().equals(currentUser.getEmail())) {
            checkEmailExists(newUserDto.getEmail());
        }
        return userRepository.updateUser(userId, newUserDto);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteUser(id);
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    private void checkEmailExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email " + email + " уже существует");
        }
    }
}
