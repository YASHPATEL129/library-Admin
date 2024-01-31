package com.libraryAdmin.Controllers;

import com.libraryAdmin.consts.Message;
import com.libraryAdmin.enums.AdminImageTypes;
import com.libraryAdmin.enums.AttachmentTypes;
import com.libraryAdmin.pojo.response.Success;
import com.libraryAdmin.service.AttachmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1")
public class AttachmentServiceController {

    @Autowired
    private AttachmentService attachmentService;

    @PostMapping("/upload-file")
    public ResponseEntity<Success<?>> uploadFile(@RequestHeader("FileTypes") AttachmentTypes attachmentTypes,
                                                 @RequestParam("File") MultipartFile file,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setMessage(Message.SUCCESS);
        success.setData(attachmentService.uploadFile(attachmentTypes, file, request, response));
        success.setMessageCode(Message.GET_SUCCESSFUL);
        return respBuilder.body(success);
    }


    @PutMapping("/updates-file/{newFilename}")
    public ResponseEntity<Success<?>> updateImage(@PathVariable String newFilename,
                                                  @RequestParam("File") MultipartFile file) {
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setMessage(Message.SUCCESS);
        success.setMessageCode(Message.GET_SUCCESSFUL);
        success.setData(attachmentService.updateFile(newFilename, file));
        return respBuilder.body(success);
    }
}
