package com.kropotov.asrd.services.storages;

import com.kropotov.asrd.entities.docs.File;
import com.kropotov.asrd.entities.docs.util.FileType;
import com.kropotov.asrd.services.FileServiceFasad;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class FileServiceFasadImp implements FileServiceFasad {


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
    public void uploadFile (MultipartFile file, String userFileName, FileType type) throws IOException {
        if (file.getBytes().length != 0) {
            String fileName = type.getDirectory() + currentDate() + "." + determineFileExtension(file);
            putFileInStorage(file, fileName, type.getDirectory());
             save(new File(fileName, userFileName, type));
        }
    }

    @Override
    public byte[] downloadFile(Long id) {
        return new byte[0];
    }

    @Override
    public void deleteFile(Long id) {

    }

}
