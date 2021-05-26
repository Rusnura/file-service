package service.components;

import org.springframework.stereotype.Component;
import service.entities.FSDirectory;
import service.entities.FSObject;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class FileSystem {
  public static final FSDirectory root = new FSDirectory("");

  public Optional<? extends FSObject> getFileByPath(String path, FSDirectory directory) {
    String[] files = path.split("/");
    String currentFile = files[0];

    for (FSObject object : directory.getChildren()) {
      if (!currentFile.equals(object.getName()))
        continue;

      if (object instanceof FSDirectory && files.length > 1)
        return getFileByPath(files[1], (FSDirectory) object);

      return Optional.of(object);
    }
    return Optional.empty();
  }
}
