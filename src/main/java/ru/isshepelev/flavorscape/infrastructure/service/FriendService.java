package ru.isshepelev.flavorscape.infrastructure.service;

import ru.isshepelev.flavorscape.infrastructure.persistance.entity.User;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.UserFriend;
import ru.isshepelev.flavorscape.infrastructure.service.dto.FriendDto;
import ru.isshepelev.flavorscape.infrastructure.service.dto.FriendRequestDto;

import java.util.List;

public interface FriendService {


    void sendFriendRequest(String senderUsername, Long recipientId);

    void acceptFriendRequest(Long requestId, String username);

    void rejectFriendRequest(Long requestId, String username);

    void blockedUserRequest(Long requestId, String username);

    void removeFromFriendsRequest(Long requestId, String username);

    List<FriendRequestDto> getPendingRequests(Long userId);

    List<FriendDto> getFriends(Long userId);
}
