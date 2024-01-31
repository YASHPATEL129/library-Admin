package com.libraryAdmin.Controllers;

import com.libraryAdmin.consts.Message;
import com.libraryAdmin.enums.AdminImageTypes;
import com.libraryAdmin.pojo.response.Success;
import com.libraryAdmin.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1")
public class ImageServiceController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload-image")
    public ResponseEntity<Success<?>> uploadImage(@RequestHeader("ImageType") AdminImageTypes imageType,
                                                  @RequestParam("File") MultipartFile file,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) {
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setMessage(Message.SUCCESS);
        success.setData(imageService.uploadImage(imageType, file, request, response));
        success.setMessageCode(Message.GET_SUCCESSFUL);
        return respBuilder.body(success);
    }

    @GetMapping("/download/{newImageName}")
    public ResponseEntity<Success<?>> downloadImage(@PathVariable String newImageName,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        imageService.downloadImage(newImageName, request, response);
        Success<?> success = new Success<>();
        success.setMessage(Message.SUCCESS);
        success.setMessageCode(Message.GET_SUCCESSFUL);
        return respBuilder.body(success);
    }

    @PutMapping("/updates-image/{newImageName}")
    public ResponseEntity<Success<?>> updateImage(@PathVariable String newImageName,
                                                  @RequestParam("file") MultipartFile file) {
        ResponseEntity.BodyBuilder respBuilder = ResponseEntity.ok();
        Success<?> success = new Success<>();
        success.setMessage(Message.SUCCESS);
        success.setMessageCode(Message.GET_SUCCESSFUL);
        success.setData(imageService.updateImage(newImageName, file));
        return respBuilder.body(success);
    }
}
