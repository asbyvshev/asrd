package com.kropotov.asrd.services.springdatajpa.docs;

import com.kropotov.asrd.entities.docs.File;
import com.kropotov.asrd.entities.docs.util.FileType;
import com.kropotov.asrd.repositories.FileRepository;
import com.kropotov.asrd.services.FileService;


import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${storage.location}")
    private Path storePath;

    private final FileRepository fileRepository;
    private final MinioClient minioClient;

    private void putFileInStorage(MultipartFile file, String fileName, String bucket) throws IOException {
        Path targetLocation = storePath.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        log.info("File {} has been succesfully uploaded!", fileName);

        // TODO: 31.05.2020  MINIO
        //        InputStream stream = file.getInputStream();
        //        Long fileSize = file.getSize();
        //        PutObjectOptions options = new PutObjectOptions(fileSize, fileSize);
        //        try {
        //            minioClient.putObject(bucket,fileName,stream,options);
        //        } catch (ErrorResponseException e) {
        //            e.printStackTrace();
        //        } catch (InsufficientDataException e) {
        //            e.printStackTrace();
        //        } catch (InternalException e) {
        //            e.printStackTrace();
        //        } catch (InvalidBucketNameException e) {
        //            e.printStackTrace();
        //        } catch (InvalidKeyException e) {
        //            e.printStackTrace();
        //        } catch (InvalidResponseException e) {
        //            e.printStackTrace();
        //        } catch (NoSuchAlgorithmException e) {
        //            e.printStackTrace();
        //        } catch (XmlParserException e) {
        //            e.printStackTrace();
        //        }


    }

    private boolean removeFromStorage (Long id){
        File file = fileRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Oops! Review " + id + " wasn't found!")
        );

        return true;
    }

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


    public File uploadFile (MultipartFile file, String userFileName, FileType type) throws IOException {
        if (file.getBytes().length != 0) {
            String fileName = type.getDirectory() + currentDate() + "." + determineFileExtension(file);
            putFileInStorage(file, fileName, type.getDirectory());
            return save(new File(fileName, userFileName, type));
        } else {
            return null;
        }
    }


    @Override
    public Optional<File> getById(Long aLong) {
        return fileRepository.findById(aLong);
    }


    @Override
    public File save(File object) {
        return fileRepository.save(object);
    }

    @Override
    public Optional<List<File>> getAll() {
        if (fileRepository.findAll() == null) {
            return Optional.empty();
        } else {
            return Optional.of(fileRepository.findAll());
        }
    }

    @Override
    public void deleteById(Long aLong) {
        removeFromStorage(aLong);
        fileRepository.deleteById(aLong);
    }
    // TODO: 28.05.2020
    //  выгрузка файла как картинки для представления на фронте  public BufferedImage loadFile(String id) {}
    //  удаление файла из БД и физического хранилища
    //  реализовать хранение файлов в MINIO + работу с эти сервисом.
}
