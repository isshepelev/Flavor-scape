package ru.isshepelev.flavorscape.infrastructure.service;

import ru.isshepelev.flavorscape.infrastructure.persistance.entity.User;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.UserFriend;
import ru.isshepelev.flavorscape.infrastructure.service.dto.FriendDto;
import ru.isshepelev.flavorscape.infrastructure.service.dto.FriendRequestDto;

import java.util.List;

public interface FriendService {

    void sendFriendRequest(Long senderId, Long recipientId);

    void acceptFriendRequest(Long requestId, Long userId);

    void rejectFriendRequest(Long requestId, Long userId);

    List<FriendRequestDto> getPendingRequests(Long userId);

    List<FriendDto> getFriends(Long userId);
}
