package com.project.doctorhub.chat.model;

import com.project.doctorhub.base.model.BaseEntity;
import com.project.doctorhub.storageFile.model.StorageFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageFile
        extends BaseEntity<Long> {

    @OneToOne
    @JoinColumn(name = "storage_file_id")
    private StorageFile file;

    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(name = "chat_message_id")
    private ChatMessage chatMessage;


}
