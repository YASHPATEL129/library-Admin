package com.libraryAdmin.service.Impl;

import com.libraryAdmin.consts.ErrorKeys;
import com.libraryAdmin.consts.Message;
import com.libraryAdmin.entity.AdminImage;
import com.libraryAdmin.entity.Attachment;
import com.libraryAdmin.entity.Book;
import com.libraryAdmin.entity.Category;
import com.libraryAdmin.exception.InvalidCredentialsException;
import com.libraryAdmin.exception.NotFoundException;
import com.libraryAdmin.interfaceProjections.BookProjection;
import com.libraryAdmin.model.params.BookUpdateParam;
import com.libraryAdmin.model.params.UploadBookParam;
import com.libraryAdmin.pojo.CurrentSession;
import com.libraryAdmin.pojo.response.BookResponse;
import com.libraryAdmin.pojo.response.BookUpdateResponse;
import com.libraryAdmin.pojo.response.UploadBookResponse;
import com.libraryAdmin.repository.AdminImageRepository;
import com.libraryAdmin.repository.AttachmentRepository;
import com.libraryAdmin.repository.BookRepository;
import com.libraryAdmin.repository.CategoryRepository;
import com.libraryAdmin.service.BookService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private AdminImageRepository adminImageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CurrentSession currentSession;

    @Override
    public UploadBookResponse uploadBook(UploadBookParam param) {
        // Check if newImageName exists in AdminImage
        AdminImage existingAdminImage = adminImageRepository.findByNewImageName(param.getNewImageName());
        boolean isImageNameExists = existingAdminImage != null;

        // Check if newFilename exists in Attachment
        Attachment existingAttachment = attachmentRepository.findByNewFilename(param.getNewFilename());
        boolean isFileNameExists = existingAttachment != null;

        // Check if the category exists in the Category table
        Category category = categoryRepository.findByCategoryName(param.getCategory());
        boolean isCategoryExists = category != null;

        if (!isImageNameExists || !isFileNameExists || !isCategoryExists) {
            // Any of the conditions is false, show the error
            throw new InvalidCredentialsException();
        }
        Book book = new Book();
        book.setTitle(param.getTitle());
        book.setDescription(param.getDescription());
        book.setPages(param.getPages());
        book.setIsbn(param.getIsbn());
        book.setPublisher(param.getPublisher());
        book.setAuthor(param.getAuthor());
        book.setCategory(category.getId()); // Set the category ID in the book entity
        book.setCreatedBy(currentSession.getUserName());
        // Save the book entity
        bookRepository.save(book);


        // Set the bookId as bind_id for the existingAdminImage and existingAttachment
        existingAdminImage.setBindId(book.getBookId());
        existingAttachment.setBindId(book.getBookId());

        // Save the updated AdminImage and Attachment entities
        adminImageRepository.save(existingAdminImage);
        attachmentRepository.save(existingAttachment);

        // Create the response DTO and return the response
        UploadBookResponse response = new UploadBookResponse();
        response.setTitle(book.getTitle());
        response.setDescription(book.getDescription());
        response.setIsbn(book.getIsbn());
        response.setPublisher(book.getPublisher());
        response.setAuthor(book.getAuthor());
        response.setCategory(category.getId());
        response.setPages(book.getPages());
        response.setCreatedBy(book.getCreatedBy());
        response.setIsPrime(book.getIsPrime());
        return response;
    }

    @Override
    public BookResponse getBook(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book =optionalBook.get();
            BookResponse response = new BookResponse();
            response.setBookId(book.getBookId());
            response.setTitle(book.getTitle());
            response.setAuthor(book.getAuthor());
            response.setCategory(book.getCategory());
            response.setIsbn(book.getIsbn());
            response.setDescription(book.getDescription());
            response.setCreatedBy(book.getCreatedBy());
            response.setPublisher(book.getPublisher());
            response.setModifiedBy(book.getModifiedBy());
            response.setModifiedDate(book.getModifiedDate());
            response.setCreatedDate(book.getCreatedDate());
            response.setModifiedBy(book.getModifiedBy());
            response.setIsDeleted(book.getIsDeleted());
            response.setDeletedDate(book.getDeletedDate());
            response.setPages(book.getPages());


            // Find the AdminImage and Attachment based on the book's bind_id
            AdminImage adminImage = adminImageRepository.findAdminImageByBindId(book.getBookId());
            Attachment attachment = attachmentRepository.findAttachmentByBindId(book.getBookId());
           try {
               if (adminImage != null && attachment != null){
                   response.setCover(adminImage.getNewImageName());
                   response.setFile(attachment.getNewFilename());
               }
           }catch (Exception e){
               throw new NotFoundException(Message.NOT_FOUND, ErrorKeys.NOT_FOUND);
           }
        return response;
        }else {
            throw new NotFoundException(Message.NOT_FOUND, ErrorKeys.NOT_FOUND);
        }
    }

    @Override
    public void deleteBook(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();

            if (book.getIsDeleted() == true) {
                throw new InvalidCredentialsException(Message.IS_ALREADY_DELETED, ErrorKeys.IS_ALREADY_DELETED,new Object[]{id});
            }
            // Set the boolean field isDeleted to true
            book.setIsDeleted(true);
            book.setDeletedDate(LocalDateTime.now());
            bookRepository.save(book);

        } else {
            throw new NotFoundException(Message.NOT_FOUND, ErrorKeys.NOT_FOUND);
        }

    }

    @Override
    public void restoreBook(Long id) {
        Optional<Book> optionalEntity = bookRepository.findById(id);
        if (optionalEntity.isPresent()) {
            Book book = optionalEntity.get();
            book.setIsDeleted(false);
            book.setDeletedDate(null);
            bookRepository.save(book);
        }
    }

    @Override
    public List<BookProjection> getAllBooks() {
        return bookRepository.findAllBook();
    }

    @Override
    public BookUpdateResponse updateBook(Long id, BookUpdateParam param) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();

            // Update the book fields from the requestDTO if available
            if (param != null) {
                if (param.getTitle() != null) {
                    book.setTitle(param.getTitle());
                }
                if (param.getDescription() != null) {
                    book.setDescription(param.getDescription());
                }
                if (param.getIsbn() != null) {
                    book.setIsbn(param.getIsbn());
                }
                if (param.getPublisher() != null) {
                    book.setPublisher(param.getPublisher());
                }
                if (param.getAuthor() != null) {
                    book.setAuthor(param.getAuthor());
                }
                if (param.getCategory() != null) {
                    Optional<Category> optionalCategory = Optional.ofNullable(categoryRepository.findByCategoryName(param.getCategory()));
                    if (optionalCategory.isPresent()) {
                        book.setCategory(optionalCategory.get().getId());
                    } else {
                        // Handle if category name is not found
                        throw new NotFoundException(Message.NOT_FOUND, ErrorKeys.NOT_FOUND);
                    }
                }
                if (param.getPages() != null) {
                    book.setPages(param.getPages());
                }
            }

            book.setModifiedBy(currentSession.getUserName());

            // Save the updated book
            bookRepository.save(book);

            BookUpdateResponse response = new BookUpdateResponse();
            response.setId(id);
            response.setTitle(book.getTitle());
            response.setDescription(book.getDescription());
            response.setIsbn(book.getIsbn());
            response.setPublisher(book.getPublisher());
            response.setAuthor(book.getAuthor());
            response.setCategory(book.getCategory());
            response.setPages(book.getPages());




            // Find the AdminImage and Attachment based on the book's bind_id
            AdminImage adminImage = adminImageRepository.findAdminImageByBindId(book.getBookId());
            Attachment attachment = attachmentRepository.findAttachmentByBindId(book.getBookId());

            if (attachment != null) {
                response.setAttachmentNewFilename(attachment.getNewFilename());
            } else {
                response.setAttachmentNewFilename(null);
            }


            if (adminImage != null) {
                response.setAdminImageNewImageName(adminImage.getNewImageName());
            } else {
                response.setAdminImageNewImageName(null);
            }
        return response;

    }else {
            throw new NotFoundException(Message.NOT_FOUND, ErrorKeys.NOT_FOUND);
        }
    }

    @Override
    public List<BookProjection> getSameBooks() {
        return bookRepository.findBookDetailsWithImageAndFile();
    }

    @Override
    public List<BookProjection> searchBook(String query, String categoryIds) {
                return bookRepository.searchBooks(query, categoryIds);
    }
}