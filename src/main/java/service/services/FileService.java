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
import java.util.*;

@Service
public class FileService {
  @Value("#{'${service.base-dir}'.split(',')}")
  private List<String> baseDirectoriesPropertyValue;
  private final Map<String, File> baseDirectories = new HashMap<>();

  @PostConstruct
  private void init() {
    for (String path: baseDirectoriesPropertyValue) {
      File file = new File(path.trim());
      if (!file.exists() || !file.canRead() || file.getName().isEmpty()) {
        System.err.println("File " + path + " isn't exists or not readable!");
        continue;
      }
      this.baseDirectories.put(file.getName(), file);
    }
  }

  public service.models.File putFile(String parent, String path, MultipartFile file, boolean override, boolean createParentFolders) throws IOException {
    File directory = checkAvailability(parent, path, true, createParentFolders);
    String originalFileName = file.getOriginalFilename();
    if (StringUtils.isEmpty(originalFileName) || StringUtils.isEmpty(originalFileName.trim()))
      throw new IllegalArgumentException("Request doesn't contains 'originalFilename'!");

    File newFile = new File(directory, originalFileName);
    if (!override && newFile.exists())
      throw new FileExistsException("File: " + originalFileName + " already exists");
    file.transferTo(newFile);
    return new service.models.File(newFile);
  }

  public List<service.models.File> getFiles(String parent, String path, boolean showHiddenFiles) throws IOException {
    List<service.models.File> files = new ArrayList<>();
    if (!parent.equals("/")) {
      File directory = checkAvailability(parent, path, true);

      File[] directoryFiles = directory.listFiles();
      if (directoryFiles != null) {
        for (File file : directoryFiles) {
          if (showHiddenFiles || !file.isHidden()) {
            files.add(new service.models.File(file));
          }
        }
      }
    } else {
      for (Map.Entry<String, File> baseDirectory : baseDirectories.entrySet()) {
        files.add(new service.models.File(baseDirectory.getValue()));
      }
    }
    return files;
  }

  public service.models.File getFile(String parent, String path) throws IOException {
    File file = checkAvailability(parent, path, false);
    return new service.models.File(file);
  }

  public boolean deleteFile(String parent, String path) throws IOException {
    File file = checkAvailability(parent, path, false);
    return file.delete();
  }

  private File checkAvailability(String parent, String path, @Nullable Boolean itsDirectory) throws IOException {
    return this.checkAvailability(parent, path, itsDirectory, false);
  }

  private File checkAvailability(String parent, String path, @Nullable Boolean itsDirectory, boolean createParentFolders) throws IOException {
    if (!baseDirectories.containsKey(parent)) {
      throw new IOException("Base directory " + parent + " isn't available!");
    }

    File baseDirectory = baseDirectories.get(parent);
    if (!baseDirectory.exists() || !baseDirectory.canRead() || !baseDirectory.isDirectory())
      throw new IOException("Base directory " + parent + " isn't available!");

    File file = new File(baseDirectory, path);

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
