package com.libraryAdmin.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCategoryResponse {

    private Long bookId;
    private String title;
    private Long categoryId;
}
