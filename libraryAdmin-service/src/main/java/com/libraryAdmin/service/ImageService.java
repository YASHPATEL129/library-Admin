package com.libraryAdmin.service;

import com.libraryAdmin.enums.AdminImageTypes;
import com.libraryAdmin.pojo.response.UploadImageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    UploadImageResponse uploadImage(AdminImageTypes imageType, MultipartFile file, HttpServletRequest request , HttpServletResponse response);

    void downloadImage(String newImageName, HttpServletRequest request , HttpServletResponse response);

    UploadImageResponse updateImage(String newImageName , MultipartFile file);
}
