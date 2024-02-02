package com.libraryAdmin.repository;

import com.libraryAdmin.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment , Long> {

    Attachment findByNewFilename(String newFilename);

    Attachment findAttachmentByBindId(Long bookId);

}
