package com.kropotov.asrd.services.storages;

import com.kropotov.asrd.entities.docs.File;
import com.kropotov.asrd.repositories.FileRepository;
import com.kropotov.asrd.services.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

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
        fileRepository.deleteById(aLong);
    }
}
