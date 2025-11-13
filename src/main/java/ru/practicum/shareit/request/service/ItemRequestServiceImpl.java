package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestDto;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    @Transactional
    public ItemRequestDto createRequest(Long userId, ItemRequestDto requestDto) {
        User user = findUserById(userId);

        ItemRequest itemRequest = itemRequestMapper.toEntity(requestDto);
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());

        return itemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getRequests(Long userId) {
        findUserById(userId);

        return itemRequestMapper.toDto(itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId));
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId) {
        findUserById(userId);

        return itemRequestMapper.toDto(itemRequestRepository.findByRequestorIdNotOrderByCreatedDesc(userId));
    }

    @Override
    public ItemRequestDto getRequestById(Long userId, Long requestId) {
        findUserById(userId);

        return itemRequestMapper.toDto(findItemRequestById(requestId));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private ItemRequest findItemRequestById(Long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос вещи не найден"));
    }
}
