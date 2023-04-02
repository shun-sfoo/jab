package com.matrix.jab.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "file_record")
@Data
public class FileRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
}
