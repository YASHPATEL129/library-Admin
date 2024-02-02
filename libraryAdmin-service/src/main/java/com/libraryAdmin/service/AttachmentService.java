package com.libraryAdmin.service;

import com.itextpdf.commons.utils.Base64;
import com.libraryAdmin.enums.AttachmentTypes;
import com.libraryAdmin.pojo.response.UploadFileResponse;
import com.libraryAdmin.pojo.response.UploadImageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {

    UploadFileResponse uploadFile(AttachmentTypes types, MultipartFile file, HttpServletRequest request , HttpServletResponse response);

    UploadFileResponse updateFile(String newFilename , MultipartFile file);

    void getPdfData(String newFilename , HttpServletRequest request , HttpServletResponse response);

    void downloadFile(String newFilename, HttpServletResponse response);
}

