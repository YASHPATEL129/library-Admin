package com.libraryAdmin.service.Impl;

import com.libraryAdmin.consts.AppConfigs;
import com.libraryAdmin.consts.ErrorKeys;
import com.libraryAdmin.consts.Message;
import com.libraryAdmin.entity.AdminImage;
import com.libraryAdmin.enums.AdminImageTypes;
import com.libraryAdmin.exception.InvalidCredentialsException;
import com.libraryAdmin.exception.ValidationException;
import com.libraryAdmin.helper.SystemHelper;
import com.libraryAdmin.pojo.CurrentSession;
import com.libraryAdmin.pojo.response.UploadImageResponse;
import com.libraryAdmin.repository.AdminImageRepository;
import com.libraryAdmin.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {


    @Autowired
    @Qualifier("allowedImageType")
    List<String> allowedImageTypes;

    @Value("${imageUploadPath}")
    String UPLOAD_DIR;

    @Autowired
    private AdminImageRepository adminImageRepository;

    @Autowired
    private CurrentSession currentSession;

    @Override
    public UploadImageResponse uploadImage(AdminImageTypes imageType, MultipartFile file, HttpServletRequest request, HttpServletResponse response) {

        if (!allowedImageTypes.contains(file.getContentType().toLowerCase())) {
            throw new ValidationException(Message.INVALID_IMAGE_FORMAT, ErrorKeys.INVALID_IMAGE_FORMAT);
        }
        try{
            String originalImageName = file.getOriginalFilename();
            String fileExtension = FilenameUtils.getExtension(originalImageName);
            String newImageName = SystemHelper.generateFileName() + "." + fileExtension;
            Files.copy(file.getInputStream(), Path.of(UPLOAD_DIR + File.separator + newImageName), StandardCopyOption.REPLACE_EXISTING);

            AdminImage adminImage = new AdminImage();
            adminImage.setImageTypes(imageType);
            adminImage.setOriginalImageName(originalImageName);
            adminImage.setNewImageName(newImageName);
            adminImage.setSize(file.getSize());
            adminImage.setCreatedBy(currentSession.getUserName());
            adminImageRepository.save(adminImage);

            UploadImageResponse responseDTO = new UploadImageResponse();
            responseDTO.setNewImageName(newImageName);
            return responseDTO;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void downloadImage(String newImageName, HttpServletRequest request, HttpServletResponse response) {
        AdminImage adminImage = adminImageRepository.findByNewImageName(newImageName);
        if (adminImage == null) {
        throw new InvalidCredentialsException(Message.IMAGE_NOT_FOUND);
        }
        String originalFilename = adminImage.getOriginalImageName();
        Path filePath = Paths.get(UPLOAD_DIR + File.separator + newImageName);
        Resource resource = new FileSystemResource(filePath);
        String contentType = "application/octet-stream";
        try (InputStream fis = resource.getInputStream();
             OutputStream fos = response.getOutputStream();) {

            String extension = FilenameUtils.getExtension(originalFilename);
            switch (extension.toLowerCase()) {
                case "png":
                    response.setContentType(MediaType.IMAGE_PNG_VALUE);
                    break;
                case "jpeg":
                case "jpg":
                    response.setContentType(MediaType.IMAGE_JPEG_VALUE);
                    break;
                case "gif":
                    response.setContentType(MediaType.IMAGE_GIF_VALUE);
                    break;
                default:
                    response.setContentType("application/octet-stream; charset=UTF-8");
            }

//            response.setContentLength(Math.toIntExact(fileSize));
            response.setHeader("Content-disposition", "inline;filename=" + new String(originalFilename.getBytes(StandardCharsets.UTF_8), "ISO-8859-1"));
            IOUtils.copy(fis, fos);
        } catch (Exception e) {
            e.printStackTrace();
//            throw new RecordNotFoundException(MessageKeys.NOT_FOUND, ErrorKeys.NOT_FOUND);
        }

    }

    @Override
    public UploadImageResponse updateImage(String newImageName, MultipartFile file) {
        AdminImage adminImage = adminImageRepository.findByNewImageName(newImageName);
        if (!allowedImageTypes.contains(file.getContentType().toLowerCase())) {
            throw new ValidationException(Message.INVALID_IMAGE_FORMAT, ErrorKeys.INVALID_IMAGE_FORMAT);
        }

        try {
            String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String updatedFileName = SystemHelper.generateFileName() + fileExtension;
            Files.copy(file.getInputStream(), Path.of(UPLOAD_DIR + File.separator + updatedFileName), StandardCopyOption.REPLACE_EXISTING);

            // Remove the old file from the folder
            String oldFileName = adminImage.getNewImageName();
            File oldFile = new File(UPLOAD_DIR + File.separator + oldFileName);
            if (oldFile.exists()) {
                oldFile.delete();
            }

            adminImage.setOriginalImageName(file.getOriginalFilename());
            adminImage.setNewImageName(updatedFileName);
            adminImage.setSize(file.getSize());
            adminImage.setModifiedBy(currentSession.getUserName());
            adminImageRepository.save(adminImage);

            UploadImageResponse responseDTO = new UploadImageResponse();
            responseDTO.setNewImageName(updatedFileName);
            return responseDTO;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
