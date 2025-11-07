package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email пользователя уже существует");
        }

        User user = UserMapper.toEntity(userDto);
        userRepository.save(user);
        return UserMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UserUpdateDto newUserDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (newUserDto.getEmail() != null && !newUserDto.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(newUserDto.getEmail())) {
                throw new EmailAlreadyExistsException("Email уже существует");
            }
            existingUser.setEmail(newUserDto.getEmail());
        }

        if (newUserDto.getName() != null) {
            existingUser.setName(newUserDto.getName());
        }

        User updatesUser = userRepository.save(existingUser);
        return UserMapper.toDto(updatesUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return UserMapper.toDto(user);
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .toList();
    }
}
