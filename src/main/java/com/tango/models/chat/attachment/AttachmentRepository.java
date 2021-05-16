package com.tango.models.chat.attachment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    @Query("select a from Attachment a where a.chatRoom.chatId=?1 order by a.message.posted desc")
    Page<Attachment> findAllByChatIdOrderByDesc(long chatId, Pageable pageable);
}
