package com.kropotov.asrd.facades;

import com.kropotov.asrd.entities.docs.File;
import com.kropotov.asrd.entities.docs.util.FileType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface FileServiceFacade {
    void uploadFile(MultipartFile doc, File file/*String userFileName, FileType type*/) /*throws IOException*/;
    byte[] downloadFile(Long id);
    void deleteFile(Long id);
}
