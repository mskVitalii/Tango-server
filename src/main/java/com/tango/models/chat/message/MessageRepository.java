package com.tango.models.chat.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select u from Message u where u.chatUser.user.Id=?1")
    List<Message> getAllByUserId(long userId);
}
