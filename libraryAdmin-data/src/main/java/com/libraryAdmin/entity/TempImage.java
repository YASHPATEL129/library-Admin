package com.libraryAdmin.entity;

import com.libraryAdmin.enums.AdminImageTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;



    private String originalImageName;

    private String newImageName;

    private String createdBy;

    private Long imageId;
    
    @Enumerated(EnumType.STRING)
    private AdminImageTypes imageTypes;
}
