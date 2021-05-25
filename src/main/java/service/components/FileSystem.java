package service.components;

import org.springframework.stereotype.Component;
import service.entities.FSDirectory;
import service.entities.FSObject;

@Component
public class FileSystem {
  private static final FSDirectory root = new FSDirectory();

  public FSObject getByPath(String path) {
    String[] segments = path.split("//");
    return null;
  }

  public FSObject getObjectByNameFromFSDirectory(String name, FSDirectory directory) {
    for (FSObject fsObject : directory.getChildren()) {
      if (fsObject.getName().equals(name))
        return fsObject;
    }

    return null;
  }

  public FSDirectory getRoot() {
    return root;
  }
}
