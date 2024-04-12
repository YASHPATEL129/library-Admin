package com.libraryAdmin.repository;

import com.libraryAdmin.entity.AdminImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AdminImageRepository extends JpaRepository<AdminImage , Long> {

    AdminImage findByNewImageName(String newImageName);

    AdminImage findAdminImageByBindId(Long bookId);

    @Query(nativeQuery = true, value = "UPDATE admin_image SET bind_id = :bookId WHERE id = :imageId" )
    @Modifying
    @Transactional
    void modifiedBindId(Long bookId, Long imageId);
}
