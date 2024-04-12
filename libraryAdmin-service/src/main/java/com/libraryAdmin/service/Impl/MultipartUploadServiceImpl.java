package com.libraryAdmin.service.Impl;

import com.libraryAdmin.consts.ErrorKeys;
import com.libraryAdmin.consts.Message;
import com.libraryAdmin.entity.*;
import com.libraryAdmin.enums.AdminImageTypes;
import com.libraryAdmin.enums.AttachmentTypes;
import com.libraryAdmin.exception.ValidationException;
import com.libraryAdmin.helper.SystemHelper;
import com.libraryAdmin.pojo.BulkUpload;
import com.libraryAdmin.pojo.CurrentSession;
import com.libraryAdmin.repository.*;
import com.libraryAdmin.service.MultipartUploadService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MultipartUploadServiceImpl implements MultipartUploadService {

    @Autowired
    @Qualifier("allowedImageType")
    List<String> allowedImageTypes;

    @Value("${imageUploadPath}")
    String UPLOAD_DIR;

    @Autowired
    private AdminImageRepository adminImageRepository;

    @Autowired
    private CurrentSession currentSession;

    @Autowired
    private TempImageRepository tempImageRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    @Qualifier("allowedFileType")
    List<String> allowedFileTypes;

    @Value("${fileUploadPath}")
    String UPLOAD_DIR_FILE;

    @Autowired
    private TempAttachmentRepository tempAttachmentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository  bookRepository;


    @Override
    public void uploadImage(AdminImageTypes imageType, MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) {
        for (MultipartFile file : files) {
            if (!allowedImageTypes.contains(file.getContentType().toLowerCase())) {
                throw new ValidationException(Message.INVALID_IMAGE_FORMAT, ErrorKeys.INVALID_IMAGE_FORMAT);
            }
        }
        for (MultipartFile file : files) {

            try {
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

                TempImage tempImage = new TempImage();
                tempImage.setImageId(adminImage.getId());
                tempImage.setOriginalImageName(adminImage.getOriginalImageName());
                tempImage.setNewImageName(adminImage.getNewImageName());
                tempImage.setCreatedBy(adminImage.getCreatedBy());
                tempImage.setImageTypes(adminImage.getImageTypes());
                tempImageRepository.save(tempImage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void uploadFile(AttachmentTypes types, MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) {
        for (MultipartFile file : files) {
            if (!allowedFileTypes.contains(file.getContentType().toLowerCase())) {
                throw new ValidationException(Message.INVALID_IMAGE_FORMAT, ErrorKeys.INVALID_IMAGE_FORMAT);
            }
        }
        for (MultipartFile file : files) {
            try {
                String originalFilename = file.getOriginalFilename();
                String fileExtension = FilenameUtils.getExtension(originalFilename);
                String newFilename = SystemHelper.generateFileName() + "." + fileExtension;
                Files.copy(file.getInputStream(), Path.of(UPLOAD_DIR_FILE + File.separator + newFilename), StandardCopyOption.REPLACE_EXISTING);

                Attachment attachment = new Attachment();
                attachment.setOriginalFilename(originalFilename);
                attachment.setSize(file.getSize());
                attachment.setFileTypes(types);
                attachment.setNewFilename(newFilename);
                attachment.setCreatedBy(currentSession.getUserName());
                attachmentRepository.save(attachment);

                TempAttachment tempAttachment = new TempAttachment();
                tempAttachment.setAttachmentId(attachment.getId());
                tempAttachment.setOriginalFilename(attachment.getOriginalFilename());
                tempAttachment.setNewFilename(attachment.getNewFilename());
                tempAttachment.setCreatedBy(attachment.getCreatedBy());
                tempAttachment.setFileTypes(attachment.getFileTypes());
                tempAttachmentRepository.save(tempAttachment);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Object bulkUpload(MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        if (file.isEmpty()) {

        }
        List<Map> results = new ArrayList<Map>();
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            // Parse the CSV file
            parseCsvData(reader).stream().forEach(e-> {
                // Check if newImageName exists in AdminImage
                Map map = new HashMap(){{
                put("Data",e.getTitle());
                }};
                TempImage imageFile = tempImageRepository.findByOriginalImageNameAndCreatedBy(e.getOriginalImageName(), currentSession.getUserName());
                if ((ObjectUtils.isEmpty(imageFile))){
                    map.put("Error" ,"Image file not found");
                    results.add(map);
                    return;
                }
                TempAttachment afile = tempAttachmentRepository.findByOriginalFilenameAndCreatedBy(e.getOriginalFilename(), currentSession.getUserName());
                if ((ObjectUtils.isEmpty(afile))){
                    map.put("Error" ,"Attachment not found");
                    results.add(map);
                    return;
                }
                // Check if the category exists in the Category table
                Category category = categoryRepository.findByCategoryName(e.getCategory());
                if ((ObjectUtils.isEmpty(afile))){
                    map.put("Error" ,"Category not found");
                    results.add(map);
                    return;
                }

                Book book = new Book();
                book.setTitle(e.getTitle());
                book.setDescription(e.getDescription());
                book.setPages(e.getPages());
                book.setIsbn(e.getIsbn());
                book.setPublisher(e.getPublisher());
                book.setAuthor(e.getAuthor());
                book.setCategory(category.getId()); // Set the category ID in the book entity
                book.setCreatedBy(currentSession.getUserName());
                book.setIsPrime(e.getIsPrime());
                // Save the book entity
                bookRepository.save(book);

                // Save the updated AdminImage and Attachment entities
                adminImageRepository.modifiedBindId(book.getBookId(), imageFile.getImageId());
                attachmentRepository.modifiedBindId(book.getBookId(), afile.getAttachmentId());

            });


        } catch (IOException e) {
            e.printStackTrace();

        }
        return results;
    }

    private List<BulkUpload> parseCsvData(Reader reader) throws IOException {
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
        return csvParser.stream().skip(1).map(record -> new BulkUpload()
                .setTitle(record.get(0))
                .setDescription(record.get(1))
                .setIsbn(record.get(2))
                .setPublisher(record.get(3))
                .setAuthor(record.get(4))
                .setCategory(record.get(5))
                .setPages(Integer.valueOf(record.get(6)))
                .setIsPrime(Boolean.valueOf(record.get(7)))
                .setOriginalFilename(record.get(8))
                .setOriginalImageName(record.get(9))).collect(Collectors.toList());

    }
}
