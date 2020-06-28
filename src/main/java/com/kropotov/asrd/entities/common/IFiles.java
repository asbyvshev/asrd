package com.kropotov.asrd.entities.common;

import com.kropotov.asrd.entities.docs.File;

import java.util.List;

public interface IFiles {
  boolean  addFile(File file);
  boolean addAllFiles(List<File> filesList);
  boolean removeFile(File file);
}
