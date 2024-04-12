package com.libraryAdmin.service;

import com.libraryAdmin.enums.AdminImageTypes;
import com.libraryAdmin.enums.AttachmentTypes;
import com.libraryAdmin.pojo.response.UploadFileResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MultipartUploadService {

    void uploadImage(AdminImageTypes imageType, MultipartFile[] files, HttpServletRequest request , HttpServletResponse response);

    void uploadFile(AttachmentTypes types, MultipartFile[] files, HttpServletRequest request , HttpServletResponse response);

    Object bulkUpload(MultipartFile file, HttpServletRequest request , HttpServletResponse response);

}
