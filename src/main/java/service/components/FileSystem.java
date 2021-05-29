package service.components;

import org.springframework.stereotype.Component;
import service.entities.FSDirectory;
import service.entities.FSFile;
import service.entities.FSObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class FileSystem {
  public static final FSDirectory root = new FSDirectory("");

  public Optional<? extends FSObject> getFSObjectByPath(String path, FSDirectory directory) {
    String[] files = path.split("/", 2);
    String currentFile = files[0];

    for (FSObject object : directory.getChildren()) {
      if (!currentFile.equals(object.getName()))
        continue;

      if (object instanceof FSDirectory && files.length > 1)
        return getFSObjectByPath(files[1], (FSDirectory) object);

      return Optional.of(object);
    }
    return Optional.empty();
  }

  public boolean addFSObjectToPath(FSObject object, String to) {
    Optional<? extends FSObject> directoryOpt = getFSObjectByPath(to, FileSystem.root);
    if (!directoryOpt.isPresent())
      return false;

    FSObject directory = directoryOpt.get();
    if (!(directory instanceof FSDirectory))
      return false;

    ((FSDirectory) directory).getChildren().add(object);
    object.setParent((FSDirectory) directory);
    return true;
  }

  public boolean removeFSObjectByPath(String path) {
    Optional<? extends FSObject> FSObjectOpt = getFSObjectByPath(path, FileSystem.root);
    if (!FSObjectOpt.isPresent())
      return false;

    FSObject fsObject = FSObjectOpt.get();
    if (fsObject instanceof FSFile) {
      FSDirectory parent = fsObject.getParent();
      return parent.getChildren().remove(fsObject);
    }

    return false;
  }
}
