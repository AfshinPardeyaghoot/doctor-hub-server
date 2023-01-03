package com.project.doctorhub.chat.model;

import com.project.doctorhub.base.model.BaseEntity;
import com.project.doctorhub.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_message")
public class ChatMessage extends BaseEntity<Long> {


    @ManyToOne(fetch = FetchType.LAZY)
    private Chat chat;
    @ManyToOne(fetch = FetchType.LAZY)
    private User sendBy;
    @Column(name = "content", length = 5000)
    private String content;
    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ChatMessageContentType contentType;

}
