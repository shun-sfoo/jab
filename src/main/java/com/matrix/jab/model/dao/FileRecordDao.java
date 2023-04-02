package com.matrix.jab.model.dao;

import com.matrix.jab.model.entity.FileRecord;
import org.springframework.data.repository.CrudRepository;

public interface FileRecordDao  extends CrudRepository<FileRecord, Long> {
    FileRecord findByFileName(String fileName);
}