package com.kropotov.asrd.repositories;

import com.kropotov.asrd.entities.docs.util.FileType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FileTypeRepository extends CrudRepository<FileType,Long> {
    List<FileType> findAll();
}
