package com.libraryAdmin.repository;

import com.libraryAdmin.entity.TempAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempAttachmentRepository extends JpaRepository<TempAttachment,Long> {

    TempAttachment findByOriginalFilenameAndCreatedBy(String originalImageName, String s);


}
