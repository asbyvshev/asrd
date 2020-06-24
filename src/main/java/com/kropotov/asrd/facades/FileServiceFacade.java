package com.kropotov.asrd.facades;

import com.kropotov.asrd.entities.docs.File;
import com.kropotov.asrd.entities.docs.util.FileType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


public interface FileServiceFacade {
    File uploadFile(MultipartFile doc, File file) ;
    byte[] downloadFile(Long id);
    void deleteFile(Long id);
    Optional<List<FileType>> getAllFileTypes();
}
