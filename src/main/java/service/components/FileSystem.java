package service.components;

import org.springframework.stereotype.Component;
import service.entities.FSDirectory;
import service.entities.FSFile;
import service.entities.FSObject;
import java.util.Optional;

@Component
public class FileSystem {
  public static final String SEPARATOR = "/";
  public static final FSDirectory ROOT = new FSDirectory("");

  public Optional<? extends FSObject> getFSObjectByPath(String path) {
    return getFSObjectByPath(path, FileSystem.ROOT);
  }

  public Optional<? extends FSObject> getFSObjectByPath(String path, FSDirectory from) {
    String[] files = path.split(FileSystem.SEPARATOR, 2);
    String currentFile = files[0];

    if (ROOT.getName().equals(currentFile)) {
      if (files.length == 1)
        return Optional.of(FileSystem.ROOT);
      return getFSObjectByPath(files[1], FileSystem.ROOT);
    }

    for (FSObject object : from.getChildren()) {
      if (!currentFile.equals(object.getName()))
        continue;

      if (object instanceof FSDirectory && files.length > 1)
        return getFSObjectByPath(files[1], (FSDirectory) object);

      return Optional.of(object);
    }
    return Optional.empty();
  }

  public boolean addFSObjectToPath(FSObject object, String to) {
    Optional<? extends FSObject> directoryOpt = getFSObjectByPath(to, FileSystem.ROOT);
    if (directoryOpt.isEmpty())
      return false;

    FSObject directory = directoryOpt.get();
    if (!(directory instanceof FSDirectory))
      return false;

    ((FSDirectory) directory).getChildren().add(object);
    object.setParent((FSDirectory) directory);
    return true;
  }

  public boolean removeFSObjectByPath(String path) {
    Optional<? extends FSObject> FSObjectOpt = getFSObjectByPath(path, FileSystem.ROOT);
    if (FSObjectOpt.isEmpty())
      return false;

    FSObject fsObject = FSObjectOpt.get();
    if (fsObject instanceof FSFile) {
      FSDirectory parent = fsObject.getParent();
      return parent.getChildren().remove(fsObject);
    }
    // TODO: Implement deletion of FSDirectory here...
    return false;
  }
}
