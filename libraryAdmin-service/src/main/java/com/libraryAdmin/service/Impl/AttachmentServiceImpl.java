package com.libraryAdmin.service.Impl;

import com.libraryAdmin.consts.ErrorKeys;
import com.libraryAdmin.consts.Message;
import com.libraryAdmin.entity.Attachment;
import com.libraryAdmin.enums.AttachmentTypes;
import com.libraryAdmin.exception.ValidationException;
import com.libraryAdmin.helper.SystemHelper;
import com.libraryAdmin.pojo.CurrentSession;
import com.libraryAdmin.pojo.response.UploadFileResponse;
import com.libraryAdmin.repository.AttachmentRepository;
import com.libraryAdmin.service.AttachmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    @Qualifier("allowedFileType")
    List<String> allowedFileTypes;

    @Value("${fileUploadPath}")
    String UPLOAD_DIR;

    @Autowired
    private CurrentSession currentSession;

    @Override
    public UploadFileResponse uploadFile(AttachmentTypes types, MultipartFile file, HttpServletRequest request, HttpServletResponse response) {

        if (!allowedFileTypes.contains(file.getContentType().toLowerCase())) {
            throw new ValidationException(Message.INVALID_IMAGE_FORMAT, ErrorKeys.INVALID_IMAGE_FORMAT);
        }

        try{
            String originalFilename = file.getOriginalFilename();
            String fileExtension = FilenameUtils.getExtension(originalFilename);
            String newFilename = SystemHelper.generateFileName() + "." + fileExtension;
            Files.copy(file.getInputStream(), Path.of(UPLOAD_DIR + File.separator + newFilename), StandardCopyOption.REPLACE_EXISTING);

            Attachment attachment = new Attachment();
            attachment.setOriginalFilename(originalFilename);
            attachment.setSize(file.getSize());
            attachment.setFileTypes(types);
            attachment.setNewFilename(newFilename);
            attachment.setCreatedBy(currentSession.getUserName());
            attachmentRepository.save(attachment);

            UploadFileResponse responseDTO = new UploadFileResponse();
            responseDTO.setNewFilename(newFilename);
            return responseDTO;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UploadFileResponse updateFile(String newFilename, MultipartFile file) {
        if (!allowedFileTypes.contains(file.getContentType().toLowerCase())) {
            throw new ValidationException(Message.INVALID_IMAGE_FORMAT, ErrorKeys.INVALID_IMAGE_FORMAT);
        }

        Attachment attachment = attachmentRepository.findByNewFilename(newFilename);
        try {
            String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String updatedFileName = System.currentTimeMillis() + fileExtension;
            Files.copy(file.getInputStream(), Path.of(UPLOAD_DIR + File.separator + updatedFileName), StandardCopyOption.REPLACE_EXISTING);

            // Remove the old file from the folder
            String oldFileName = attachment.getNewFilename();
            File oldFile = new File(UPLOAD_DIR + File.separator + oldFileName);
            if (oldFile.exists()) {
                oldFile.delete();
            }

            attachment.setOriginalFilename(file.getOriginalFilename());
            attachment.setNewFilename(updatedFileName);
            attachment.setSize(file.getSize());
            attachment.setModifiedBy(currentSession.getUserName());
            attachmentRepository.save(attachment);

            UploadFileResponse responseDTO = new UploadFileResponse();
            responseDTO.setNewFilename(newFilename);
            return responseDTO;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

