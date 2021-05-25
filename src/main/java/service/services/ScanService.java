package service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.components.FileSystem;

import java.io.File;
import java.io.IOException;

@Service
public class ScanService {
  @Autowired
  private FileSystem fileSystem;

  public void doScan(File directory) throws IOException {
    if (!directory.exists() || !directory.isDirectory() || !directory.canRead())
      throw new IOException("Cannot start scan directory: " + directory.getAbsolutePath());


  }
}
