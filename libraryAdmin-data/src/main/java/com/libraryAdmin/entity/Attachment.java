package com.libraryAdmin.entity;

import com.libraryAdmin.enums.AttachmentTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String originalFilename;
    private String newFilename;

    private Long size;

    private String createdBy;
    private String modifiedBy;

    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime modifiedDate;

    private Long bindId;

    @Enumerated(EnumType.STRING)
    private AttachmentTypes fileTypes;


}
