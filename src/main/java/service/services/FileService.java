package service.services;

import org.apache.commons.io.FileExistsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
public class FileService {
  @Value("${service.base-dir}")
  private String baseDirectoryPropertyValue;
  private File baseDirectory;

  @PostConstruct
  private void init() {
    this.baseDirectory = new File(baseDirectoryPropertyValue);
  }

  public service.models.File putFile(String path, MultipartFile file, boolean override, boolean createParentFolders) throws IOException {
    File directory = checkAvailability(path, true, createParentFolders);
    String originalFileName = file.getOriginalFilename();
    if (StringUtils.isEmpty(originalFileName) || StringUtils.isEmpty(originalFileName.trim()))
      throw new IllegalArgumentException("Request doesn't contains 'originalFilename'!");

    File newFile = new File(directory, originalFileName);
    if (!override && newFile.exists())
      throw new FileExistsException("File: " + originalFileName + " already exists");
    file.transferTo(newFile);
    return new service.models.File(newFile);
  }

  public List<service.models.File> getFiles(String path, boolean showHiddenFiles) throws IOException {
    File directory = checkAvailability(path, true);

    File[] directoryFiles = directory.listFiles();
    List<service.models.File> files = new LinkedList<>();
    if (directoryFiles != null) {
      for (File file : directoryFiles) {
        if (showHiddenFiles || !file.isHidden()) {
          files.add(new service.models.File(file));
        }
      }
    }
    return files;
  }

  public service.models.File getFile(String path) throws IOException {
    File file = checkAvailability(path, false);
    return new service.models.File(file);
  }

  public boolean deleteFile(String path) throws IOException {
    File file = checkAvailability(path, false);
    return file.delete();
  }

  private File checkAvailability(String path, @Nullable Boolean itsDirectory) throws IOException {
    return this.checkAvailability(path, itsDirectory, false);
  }

  private File checkAvailability(String path, @Nullable Boolean itsDirectory, boolean createParentFolders) throws IOException {
    File file = new File(baseDirectory, path);
    if (!baseDirectory.exists() || !baseDirectory.canRead() || !baseDirectory.isDirectory())
      throw new IOException("Base directory isn't available!");

    if (!file.getCanonicalPath().contains(baseDirectory.getCanonicalPath()))
      throw new IOException("File: " + file.getName() + " isn't allowed!");

    if (!file.exists()) {
      if (!createParentFolders)
        throw new IOException("File: " + file.getName() + " doesn't exists!");
      if (!file.mkdirs())
        throw new IOException("File: " + file.getName() + " cannot be created!");
    }

    if (!file.canRead())
      throw new IOException("File: " + file.getName() + " doesn't readable!");

    if (itsDirectory != null) {
      if (itsDirectory && !file.isDirectory())
        throw new IOException(file.getName() + " isn't directory, but file!");
      else if (!itsDirectory && !file.isFile())
        throw new IOException(file.getName() + " isn't file, but directory!");
    }
    return file;
  }
}
