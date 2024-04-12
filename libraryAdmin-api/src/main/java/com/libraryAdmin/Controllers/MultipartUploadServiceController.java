package com.libraryAdmin.Controllers;

import com.libraryAdmin.consts.Message;
import com.libraryAdmin.enums.AdminImageTypes;
import com.libraryAdmin.enums.AttachmentTypes;
import com.libraryAdmin.pojo.response.Success;
import com.libraryAdmin.service.MultipartUploadService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1")
public class MultipartUploadServiceController {


    @Autowired
    private MultipartUploadService multipartUploadService;

    @PostMapping("/multiple/upload-image")
    public ResponseEntity<Success<?>> uploadImage(@RequestHeader("ImageType") AdminImageTypes imageType,
                                                  @RequestParam("Files") MultipartFile[] file,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) {
        multipartUploadService.uploadImage(imageType, file, request, response);
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setMessageCode(Message.IMAGE_UPLOAD_SUCCESSFUL);
        return respBuilder.body(success);
    }

    @PostMapping("/multiple/upload-files")
    public ResponseEntity<Success<?>> uploadFile(@RequestHeader("FileTypes") AttachmentTypes attachmentTypes,
                                                  @RequestParam("Files") MultipartFile[] file,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) {
        multipartUploadService.uploadFile(attachmentTypes, file, request, response);
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setMessageCode(Message.FILE_UPLOAD_SUCCESSFUL);
        return respBuilder.body(success);
    }

    @PostMapping("/bulk/upload")
    public ResponseEntity<Success<?>> uploadFile(@RequestParam("File") MultipartFile file,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {
        Object resp = multipartUploadService.bulkUpload(file, request, response);
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setMessageCode(Message.CSV_FILE_UPLOAD_SUCCESSFUL);
        success.setData(resp);
        return respBuilder.body(success);
    }
}
