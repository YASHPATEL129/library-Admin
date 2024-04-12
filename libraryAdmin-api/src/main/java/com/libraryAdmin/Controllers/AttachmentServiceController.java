package com.libraryAdmin.Controllers;

import com.itextpdf.commons.utils.Base64;
import com.libraryAdmin.consts.ErrorKeys;
import com.libraryAdmin.consts.Message;
import com.libraryAdmin.entity.Attachment;
import com.libraryAdmin.enums.AdminImageTypes;
import com.libraryAdmin.enums.AttachmentTypes;
import com.libraryAdmin.exception.InvalidCredentialsException;
import com.libraryAdmin.exception.NotFoundException;
import com.libraryAdmin.pojo.response.Success;
import com.libraryAdmin.repository.AttachmentRepository;
import com.libraryAdmin.service.AttachmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("/v1")
public class AttachmentServiceController {

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Value("${fileUploadPath}")
    String UPLOAD_DIR;

    @PostMapping("/upload-file")
    public ResponseEntity<Success<?>> uploadFile(@RequestHeader("FileTypes") AttachmentTypes attachmentTypes,
                                                 @RequestParam("File") MultipartFile file,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setData(attachmentService.uploadFile(attachmentTypes, file, request, response));
        success.setMessageCode(Message.PDF_UPLOAD_SUCCESSFUL);
        return respBuilder.body(success);
    }


    @PutMapping("/updates-file/{newFilename}")
    public ResponseEntity<Success<?>> updateImage(@PathVariable String newFilename,
                                                  @RequestParam("File") MultipartFile file) {
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setMessageCode(Message.PDF_UPDATE_SUCCESSFUL);
        success.setData(attachmentService.updateFile(newFilename, file));
        return respBuilder.body(success);
    }




    @GetMapping(value = "/attachment/{newFilename}")
    public ResponseEntity<Success<?>> getPdfContent(@PathVariable String newFilename, HttpServletRequest request, HttpServletResponse response) {
        attachmentService.getPdfData(newFilename, request,response);
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setMessageCode(Message.DATA_GET_SUCCESSFUL);
        return respBuilder.body(success);
    }

}
