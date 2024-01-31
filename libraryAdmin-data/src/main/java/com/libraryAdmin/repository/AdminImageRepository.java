package com.libraryAdmin.repository;

import com.libraryAdmin.entity.AdminImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminImageRepository extends JpaRepository<AdminImage , Long> {

    AdminImage findByNewImageName(String newImageName);

    AdminImage findAdminImageByBindId(Long bookId);
}
