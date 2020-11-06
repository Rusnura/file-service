package service.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import service.models.FileEntity;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class FileService {
  private static final String DIRECTORIES_NODE = "directories";
  private static final String DIRECTORY_PATH_NODE = "path";
  private static final String DIRECTORY_PASSWORD_NODE = "password";
  private static final String DIRECTORY_CAN_DOWNLOAD_NODE = "canDownload";
  private static final String DIRECTORY_CAN_UPLOAD_NODE = "canUpload";
  private static final String DIRECTORY_CAN_DELETE_NODE = "canDelete";

  private final Map<String, FileEntity> baseDirectories = new HashMap<>();

  @Value("${service.config.file}")
  private String configFilePath;

  @Autowired
  private ObjectMapper objectMapper;

  @PostConstruct
  private void init() throws IOException {
    JsonNode properties;
    JsonNode directories = objectMapper.createObjectNode();
    try (InputStream configFileInputStream = getClass().getResourceAsStream(configFilePath)) {
      properties = objectMapper.readTree(configFileInputStream);
      if (properties.has(DIRECTORIES_NODE)) directories = properties.get(DIRECTORIES_NODE);
    }

    for (JsonNode directory: directories) {
      if (!directory.has(DIRECTORY_PATH_NODE)) continue;
      String path = directory.get(DIRECTORY_PATH_NODE).asText().trim();
      FileEntity file = new FileEntity(path);
      if (!file.exists() || !file.canRead() || file.getName().isEmpty()) {
        System.err.println("File " + path + " isn't exists or not readable!");
        continue;
      }

      String password = null;
      if (directory.has(DIRECTORY_PASSWORD_NODE))
        password = directory.get(DIRECTORY_PASSWORD_NODE).asText();

      boolean isCanDownload = true;
      if (directory.has(DIRECTORY_CAN_DOWNLOAD_NODE))
        isCanDownload = directory.get(DIRECTORY_CAN_DOWNLOAD_NODE).asBoolean(true);

      boolean isCanUpload = true;
      if (directory.has(DIRECTORY_CAN_UPLOAD_NODE))
        isCanUpload = directory.get(DIRECTORY_CAN_UPLOAD_NODE).asBoolean(true);

      boolean isCanDelete = true;
      if (directory.has(DIRECTORY_CAN_DELETE_NODE))
        isCanDelete = directory.get(DIRECTORY_CAN_DELETE_NODE).asBoolean(true);

      file.setPassword(password);
      file.setCanDownload(isCanDownload);
      file.setCanUpload(isCanUpload);
      file.setCanDelete(isCanDelete);
      this.baseDirectories.put(file.getName(), file);
    }
  }

  public FileEntity putFile(String path, String password, MultipartFile file, boolean override, boolean createParentFolders) throws IOException {
    File directory = checkAvailability(path, password, true, createParentFolders);
    canUpload(path);
    String originalFileName = file.getOriginalFilename();
    if (StringUtils.isEmpty(originalFileName) || StringUtils.isEmpty(originalFileName.trim()))
      throw new IllegalArgumentException("Request doesn't contains 'originalFilename'!");

    File newFile = new File(directory, originalFileName);
    if (!override && newFile.exists())
      throw new FileExistsException("File: " + originalFileName + " already exists");
    file.transferTo(newFile);
    return new FileEntity(newFile.getPath());
  }

  public List<FileEntity> getFiles(String path, String password, boolean showHiddenFiles) throws IOException {
    List<FileEntity> files = new ArrayList<>();
    if (!path.equals("/")) {
      File directory = checkAvailability(path, password, true);

      File[] directoryFiles = directory.listFiles();
      if (directoryFiles != null) {
        for (File file : directoryFiles) {
          if (showHiddenFiles || !file.isHidden()) {
            files.add(new FileEntity(file.getPath()));
          }
        }
      }
    } else {
      for (Map.Entry<String, FileEntity> baseDirectory : baseDirectories.entrySet()) {
        files.add(baseDirectory.getValue());
      }
    }
    return files;
  }

  public FileEntity getFile(String path, String password) throws IOException {
    File file = checkAvailability(path, password, false);
    canDownload(path);
    return new FileEntity(file.getPath());
  }

  public boolean deleteFile(String path, String password) throws IOException {
    File file = checkAvailability(path, password, false);
    canDelete(path);
    return file.delete();
  }

  private FileEntity getBaseDirectoryByPath(String path) throws FileNotFoundException {
    FileEntity baseDirectory = null;
    for (Map.Entry<String, FileEntity> directory : baseDirectories.entrySet()) {
      if (path.contains(directory.getKey()))
        baseDirectory = directory.getValue();
    }

    if (baseDirectory == null || !baseDirectory.exists() || !baseDirectory.canRead() || !baseDirectory.isDirectory())
      throw new FileNotFoundException("Base directory " + path + " isn't available!");
    return baseDirectory;
  }

  private void checkPasswordToBaseDirectory(FileEntity baseDirectory, String password) {
    if (!StringUtils.isEmpty(baseDirectory.getPassword()) && !password.equals(baseDirectory.getPassword()))
      throw new SecurityException("Wrong password for directory " + baseDirectory.getName() + "!");
  }

  private File checkAvailability(String path, String password, @Nullable Boolean itsDirectory) throws IOException {
    return this.checkAvailability(path, password, itsDirectory, false);
  }

  private File checkAvailability(String path, String password, @Nullable Boolean itsDirectory, boolean createParentFolders) throws IOException {
    FileEntity baseDirectory = getBaseDirectoryByPath(path);
    checkPasswordToBaseDirectory(baseDirectory, password);
    File file = new File(baseDirectory, path.replace(baseDirectory.getName(), ""));
    if (!file.getCanonicalPath().contains(baseDirectory.getCanonicalPath()))
      throw new IOException("File: " + file.getName() + " isn't allowed!");

    if (!file.exists()) {
      if (!createParentFolders)
        throw new FileNotFoundException("File: " + file.getName() + " doesn't exists!");
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

  private void canDownload(String path) throws IOException {
    if (!getBaseDirectoryByPath(path).isCanDownload())
      throw new SecurityException("You can't download from " + path);
  }

  private void canUpload(String path) throws IOException {
    if (!getBaseDirectoryByPath(path).isCanUpload())
      throw new SecurityException("You can't upload to " + path);
  }

  private void canDelete(String path) throws IOException {
    if (!getBaseDirectoryByPath(path).isCanDelete())
      throw new SecurityException("You can't delete from " + path);
  }
}
