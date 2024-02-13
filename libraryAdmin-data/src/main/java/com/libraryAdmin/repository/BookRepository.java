package com.libraryAdmin.repository;

import com.libraryAdmin.entity.Book;
import com.libraryAdmin.interfaceProjections.BookProjection;
import com.libraryAdmin.pojo.response.BookResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByCategory(Long id);

    @Query(nativeQuery = true, value = "SELECT b.book_id AS bookId, " +
            "b.title, " +
            "b.description, " +
            "b.pages, " +
            "b.isbn, " +
            "b.publisher, " +
            "b.author, " +
            "b.category, " +
            "b.created_by, " +
            "b.modified_by, " +
            "b.created_date, " +
            "b.modified_date, " +
            "b.is_deleted, " +
            "b.deleted_date, " +
            "i.new_image_name AS cover, " +
            "a.new_filename AS file " +
            "FROM Book b " +
            "LEFT JOIN admin_image i ON b.book_id = i.bind_id " +
            "LEFT JOIN Attachment a ON b.book_id = a.bind_id " +
            "ORDER BY b.book_id " +
            "LIMIT 12")
    List<BookProjection> findBookDetailsWithImageAndFile();


    @Query( nativeQuery = true, value = "SELECT b.book_id AS bookId, " +
            "b.title, " +
            "b.description, " +
            "b.pages, " +
            "b.isbn, " +
            "b.publisher, " +
            "b.author, " +
            "b.category, " +
            "b.created_by, " +
            "b.modified_by, " +
            "b.created_date, " +
            "b.modified_date, " +
            "b.is_deleted, " +
            "b.deleted_date, " +
            "i.new_image_name AS cover, " +
            "a.new_filename AS file " +
            "FROM Book b " +
            "LEFT JOIN admin_image i ON b.book_id = i.bind_id " +
            "LEFT JOIN Attachment a ON b.book_id = a.bind_id " +
            "WHERE (:query IS NULL OR " +
            "(LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.publisher) LIKE LOWER(CONCAT('%', :query, '%')))) " +
            "AND (:categoryIds IS NULL OR FIND_IN_SET(b.category, :categoryIds))")
    List<BookProjection> searchBooks(@Param("query") String query, @Param("categoryIds") String categoryIds);

    @Query(nativeQuery = true, value = "SELECT b.book_id AS bookId, " +
            "b.title, " +
            "b.description, " +
            "b.pages, " +
            "b.isbn, " +
            "b.publisher, " +
            "b.author, " +
            "b.category, " +
            "b.created_by, " +
            "b.modified_by, " +
            "b.created_date, " +
            "b.modified_date, " +
            "b.is_deleted, " +
            "b.deleted_date, " +
            "i.new_image_name AS cover, " +
            "a.new_filename AS file " +
            "FROM Book b " +
            "LEFT JOIN admin_image i ON b.book_id = i.bind_id " +
            "LEFT JOIN Attachment a ON b.book_id = a.bind_id ")
    List<BookProjection> findAllBook();
}
