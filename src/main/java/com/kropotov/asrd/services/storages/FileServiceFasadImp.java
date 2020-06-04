package com.kropotov.asrd.services.storages;

import com.kropotov.asrd.entities.docs.File;
import com.kropotov.asrd.entities.docs.util.FileType;
import com.kropotov.asrd.services.FileServiceFasad;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceFasadImp implements FileServiceFasad {

    private final FileServiceImpl fileService;
    private final MinIOSeviceImp minIOSevice;

    private String currentDate(){
        Date data = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return simpleDateFormat.format(data);
    }

    private String determineFileExtension(MultipartFile file) {

        switch (Objects.requireNonNull(file.getContentType())) {
            case MediaType.APPLICATION_PDF_VALUE:
                return "pdf";

            case MediaType.IMAGE_JPEG_VALUE:
                return "jpeg";

            case MediaType.IMAGE_PNG_VALUE:
                return "png";

            case MediaType.IMAGE_GIF_VALUE:
                return "gif";

            default:
// TODO: 28.05.2020  throw new UnsupportedMediaTypeException("Error! This file type is not supported!");
                return  "png";
        }
    }

    @Override
    public void uploadFile (MultipartFile file, String userFileName, FileType type) throws IOException {
        Assert.notNull(file,"fdgdfg");
        String fileName = type.getDirectory() + currentDate() + "." + determineFileExtension(file);
        minIOSevice.uploadFile(file, fileName, type.getDirectory());
        fileService.save(new File(fileName, userFileName, type));
    }

    @Override
    public byte[] downloadFile(Long id) {
        File file = fileService.getById(id).get();
        String bucket = file.getType().getDirectory();
        String filename = file.getTitle();
        return minIOSevice.downloadFile(bucket,filename);
    }

    @Override
    public void deleteFile(Long id) {
        File file = fileService.getById(id).get();
        String bucket = file.getType().getDirectory();
        String filename = file.getTitle();
        minIOSevice.deleteFile(bucket,filename);
        fileService.deleteById(id);
    }
}
