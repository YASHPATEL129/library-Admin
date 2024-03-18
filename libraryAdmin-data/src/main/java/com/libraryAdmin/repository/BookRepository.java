package com.libraryAdmin.repository;

import com.libraryAdmin.entity.Book;
import com.libraryAdmin.interfaceProjections.BookByIdProjection;
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
            "b.title AS title, " +
            "b.description AS description," +
            "b.pages AS pages, " +
            "b.isbn AS isbn, " +
            "b.publisher AS publisher," +
            "b.author AS author," +
            " b.category AS category," +
            " b.created_by AS createdBy," +
            " b.modified_by AS modifiedBy, " +
            "b.created_date AS createdDate," +
            " b.modified_date AS modifiedDate," +
            " b.is_deleted AS isDeleted," +
            " b.deleted_date AS deletedDate, " +
            " b.is_prime AS isPrime,"+
            "i.new_image_name AS cover, " +
            "a.new_filename AS file " +
            "FROM Book b " +
            "LEFT JOIN admin_image i ON b.book_id = i.bind_id " +
            "LEFT JOIN Attachment a ON b.book_id = a.bind_id " +
            "ORDER BY b.book_id " +
            "LIMIT 12")
    List<BookProjection> findBookDetailsWithImageAndFile();


    @Query( nativeQuery = true, value = "SELECT b.book_id AS bookId, " +
            "b.title AS title, " +
            "b.description AS description," +
            "b.pages AS pages, " +
            "b.isbn AS isbn, " +
            "b.publisher AS publisher," +
            "b.author AS author," +
            " b.category AS category," +
            " b.created_by AS createdBy," +
            " b.modified_by AS modifiedBy, " +
            "b.created_date AS createdDate," +
            " b.modified_date AS modifiedDate," +
            " b.is_deleted AS isDeleted," +
            " b.deleted_date AS deletedDate, " +
            " b.is_prime AS isPrime,"+
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
            "b.title AS title, " +
            "b.description AS description," +
            "b.pages AS pages, " +
            "b.isbn AS isbn, " +
            "b.publisher AS publisher," +
            "b.author AS author," +
            " b.category AS category," +
            " b.created_by AS createdBy," +
            " b.modified_by AS modifiedBy, " +
            "b.created_date AS createdDate," +
            " b.modified_date AS modifiedDate," +
            " b.is_deleted AS isDeleted," +
            " b.deleted_date AS deletedDate, " +
            " b.is_prime AS isPrime,"+
            "i.new_image_name AS cover, " +
            "a.new_filename AS file " +
            "FROM Book b " +
            "LEFT JOIN admin_image i ON b.book_id = i.bind_id " +
            "LEFT JOIN Attachment a ON b.book_id = a.bind_id ")
    List<BookProjection> findAllBook();







}
