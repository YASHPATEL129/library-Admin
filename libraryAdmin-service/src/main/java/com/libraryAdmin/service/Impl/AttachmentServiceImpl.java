package com.libraryAdmin.service.Impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.libraryAdmin.consts.ErrorKeys;
import com.libraryAdmin.consts.Message;
import com.libraryAdmin.entity.Attachment;
import com.libraryAdmin.enums.AttachmentTypes;
import com.libraryAdmin.exception.InvalidCredentialsException;
import com.libraryAdmin.exception.NotFoundException;
import com.libraryAdmin.exception.ValidationException;
import com.libraryAdmin.helper.SystemHelper;
import com.libraryAdmin.pojo.CurrentSession;
import com.libraryAdmin.pojo.response.UploadFileResponse;
import com.libraryAdmin.repository.AttachmentRepository;
import com.libraryAdmin.service.AttachmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Override
    public void downloadFile(String newFilename, HttpServletResponse response) {


    }

    @Override
    public void getPdfData(String newFilename , HttpServletRequest request, HttpServletResponse response){
        Attachment attachment = attachmentRepository.findByNewFilename(newFilename);
        if (attachment == null) {
            throw new NotFoundException(Message.NOT_FOUND, ErrorKeys.NOT_FOUND);
        }
        String originalFilename = attachment.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + File.separator + newFilename);
        Resource resource = new FileSystemResource(filePath);
        if (!resource.exists()) {
            throw new NotFoundException(Message.NOT_FOUND, ErrorKeys.NOT_FOUND);
        }

        String contentType = "application/octet-stream";
        try (InputStream fis = resource.getInputStream();
             OutputStream fos = response.getOutputStream();) {

            String extension = FilenameUtils.getExtension(originalFilename);
            response.setContentType(MediaType.APPLICATION_PDF_VALUE);


//            response.setContentLength(Math.toIntExact(fileSize));
            response.setHeader("Content-disposition", "inline;filename=" + new String(originalFilename.getBytes(StandardCharsets.UTF_8), "ISO-8859-1"));
            IOUtils.copy(fis, fos);
        } catch (Exception e) {
            e.printStackTrace();
//            throw new RecordNotFoundException(MessageKeys.NOT_FOUND, ErrorKeys.NOT_FOUND);
        }
    }


}

