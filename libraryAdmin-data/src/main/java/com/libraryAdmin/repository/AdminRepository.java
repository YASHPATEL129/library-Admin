package com.libraryAdmin.repository;

import com.libraryAdmin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

     boolean existsByEmail(String email);

     Admin findByEmail(String email);

     List<Admin> findByIsSuperAdmin(boolean isSuperAdmin);
}
