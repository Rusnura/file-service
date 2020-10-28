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
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class FileService {
  private static final String DIRECTORIES_NODE = "directories";
  private static final String DIRECTORY_PATH_NODE = "path";
  private final Map<String, File> baseDirectories = new HashMap<>();

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
      File file = new File(path);
      if (!file.exists() || !file.canRead() || file.getName().isEmpty()) {
        System.err.println("File " + path + " isn't exists or not readable!");
        continue;
      }
      this.baseDirectories.put(file.getName(), file);
    }
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
    List<service.models.File> files = new ArrayList<>();
    if (!path.equals("/")) {
      File directory = checkAvailability(path, true);

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
    File baseDirectory = null;
    for (Map.Entry<String, File> directory : baseDirectories.entrySet()) {
      if (path.contains(directory.getKey()))
        baseDirectory = directory.getValue();
    }

    if (baseDirectory == null || !baseDirectory.exists() || !baseDirectory.canRead() || !baseDirectory.isDirectory())
      throw new IOException("Base directory " + path + " isn't available!");

    File file = new File(baseDirectory, path.replace(baseDirectory.getName(), ""));
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
