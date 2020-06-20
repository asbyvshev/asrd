package com.kropotov.asrd.facades;

import com.kropotov.asrd.entities.docs.File;
import org.springframework.web.multipart.MultipartFile;


public interface FileServiceFacade {
    File uploadFile(MultipartFile doc, File file) ;
    byte[] downloadFile(Long id);
    void deleteFile(Long id);
}
