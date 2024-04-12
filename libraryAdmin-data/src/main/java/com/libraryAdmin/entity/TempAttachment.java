package com.libraryAdmin.entity;

import com.libraryAdmin.enums.AttachmentTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String originalFilename;

    private String newFilename;

    private String createdBy;

    private Long attachmentId;

    @Enumerated(EnumType.STRING)
    private AttachmentTypes fileTypes;
}
