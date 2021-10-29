package com.univer.universerver.source.controller;

import com.univer.universerver.source.common.response.ErrorCode;
import com.univer.universerver.source.common.response.exception.CommonException;
import com.univer.universerver.source.common.response.exception.UserException;
import com.univer.universerver.source.model.*;
import com.univer.universerver.source.model.request.chatroom.BanRequest;
import com.univer.universerver.source.model.request.chatroom.HelloMessage;
import com.univer.universerver.source.model.response.ChatRoomResponse;
import com.univer.universerver.source.model.response.MatchRoomResponse;
import com.univer.universerver.source.model.response.MessageResponse;
import com.univer.universerver.source.model.response.UserResponse;
import com.univer.universerver.source.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.lang.reflect.Array;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chatroom")
public class ChatRoomController {

    @Autowired
    ChatRoomService chatRoomService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private MatchingService matchingService;
    @Autowired
    private ChatRoomUserService chatRoomUserService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @ApiOperation(value="채팅리스트 조회",notes="채팅리스트 조회")
    @GetMapping
    public ResponseEntity<?> getMatchRoomList(Pageable pageable, Principal principal) {

        String userId = principal.getName();
        List<ChatRoomResponse> chatRoomList= chatRoomService.selectChatRoomList(userId);
        return ResponseEntity.ok(chatRoomList);

    }
    @ApiOperation(value="채팅방내용 조회",notes="채팅방내용 조회")
    @GetMapping("/{chatroomId}")
    public ResponseEntity<?> getMatchRoomInfo(Principal principal, @PathVariable(name = "chatroomId") long id) {
        List<MessageResponse> chatRoomInfo = messageService.selectChatroomInfo(id);
        return ResponseEntity.ok(chatRoomInfo);
    }
    @ApiOperation(value="채팅방내 사람 조회",notes="채팅방내 사람 조회")
    @GetMapping("/info/{chatroomId}")
    public ResponseEntity<?> getchatRoomPeopleInfo(Principal principal,@PathVariable(name = "chatroomId") long id) {
        Optional<User> user = userService.findMyUserInfo(principal.getName());
        List<UserResponse> chatRoomInfo = chatRoomService.selectchatRoomPeopleInfo(id);

        return ResponseEntity.ok(chatRoomInfo.stream().filter(chatUser->chatUser.getId()!=user.get().getId()));
    }
    @ApiOperation(value="채팅방내 사람 강퇴",notes="채팅방내 사람 강퇴")
    @PatchMapping("/ban")
    @Transactional
    public ResponseEntity<?> banUser(Principal principal,@RequestBody BanRequest param) {
        long chatroomId = param.getChatroomId();

        Optional<User> user = userService.findMyUserInfo(principal.getName());
        ChatRoom chatRoom = chatRoomService.findChatRoom(chatroomId);
        List<Matching> matchings = matchingService.findMatchRoom(chatRoom.getMatchRoom().getId());
        matchings.stream().forEach(item->{
            if(item.getUser().getId()==user.get().getId()){
                if(item.getMasterYn()=='N'){
                    throw new UserException(ErrorCode.ERR_NO_MASTER);
                }
            }
        });
        String[] ids = param.getIds();
        Arrays.stream(ids).forEach(userId-> {
            chatRoomUserService.banUsers(userId,chatroomId);
            matchingService.deleteUser(userId,chatRoom.getMatchRoom().getId());
            String msg = "ban";
            messagingTemplate.convertAndSend("/topic/ban/"+userId,msg);
        });

        return ResponseEntity.ok("");
    }

}
