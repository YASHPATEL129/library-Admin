package com.libraryAdmin.repository;

import com.libraryAdmin.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment , Long> {

    Attachment findByNewFilename(String newFilename);

    Attachment findAttachmentByBindId(Long bookId);



    @Query(nativeQuery = true, value = "UPDATE attachment SET bind_id = :bookId WHERE id = :attachmentId" )
    @Modifying
    @Transactional
    void modifiedBindId(Long bookId, Long attachmentId);
}
