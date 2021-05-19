package com.tango.models.chat.user;

import com.tango.models.chat.message.Message;
import com.tango.models.chat.room.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

    @Query("select u.chatRoom from ChatUser u where u.user.Id=?1 order by u.chatRoom.lastPosted desc")
    Page<ChatRoom> findAllByUserUserIdOrderByChatRoomLastDesc(Long userId, Pageable pageable);

    @Query("select (count(e)>0) from ChatUser e where e.chatRoom.chatId=?1 and e.user.Id=?2")
    boolean existsByChatAndUser(long chatId, long userId);

    @Query("select u from ChatUser u where u.user.Id=?1 and u.joined=false")
    Page<ChatUser> findAllInvitations(long userId, Pageable pageable);

    @Modifying
    @Query("delete from ChatUser u where u.user.Id=?1")
    void deleteAllByUserId(long userId);

//    @Query("select u from ChatUser u where u.")
//    List<Message> getAllMessagesByUserId(long userId);
}
