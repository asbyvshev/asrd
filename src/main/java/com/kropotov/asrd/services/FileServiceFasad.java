package com.kropotov.asrd.services;

import com.kropotov.asrd.entities.docs.util.FileType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface FileServiceFasad {
    void uploadFile(MultipartFile file, String userFileName, FileType type) throws IOException;
    byte[] downloadFile(Long id);
    void deleteFile(Long id);
}
