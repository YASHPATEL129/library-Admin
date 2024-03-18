package com.libraryAdmin.repository;

import com.libraryAdmin.entity.TempImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempImageRepository extends JpaRepository<TempImage, Long > {

}
