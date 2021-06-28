package service.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import service.entities.FSDirectory;
import service.entities.FSObject;
import service.validators.FSObjectNameValidator;
import service.validators.interfaces.IValidator;
import java.util.Optional;

@Component
public class FileSystem {
  private static final Logger LOGGER = LoggerFactory.getLogger(FileSystem.class);
  private static final IValidator[] NAME_VALIDATORS = new IValidator[] { new FSObjectNameValidator() };

  public static final String SEPARATOR = "/";
  public static final FSDirectory ROOT = new FSDirectory("");

  public Optional<? extends FSObject> getFSObjectByPath(String path) {
    return getFSObjectByPath(path, FileSystem.ROOT);
  }

  public Optional<? extends FSObject> getFSObjectByPath(String path, FSDirectory from) {
    String[] files = path.split(FileSystem.SEPARATOR, 2);
    String currentFile = files[0];

    if (FileSystem.ROOT.getName().equals(currentFile)) {
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
    for (IValidator nameValidator : NAME_VALIDATORS) {
      if (!nameValidator.validate(object))
        throw new IllegalArgumentException("Name '" + object.getName() + "' isn't allowed!");
    }

    Optional<? extends FSObject> directoryOpt = getFSObjectByPath(to, FileSystem.ROOT);
    if (directoryOpt.isEmpty())
      return false;

    FSObject fsObject = directoryOpt.get();
    if (!(fsObject instanceof FSDirectory))
      return false;

    FSDirectory fsDirectory = (FSDirectory) fsObject;
    if (fsDirectory.getChildren().stream().anyMatch(f -> object.getName().equals(f.getName()))) {
      LOGGER.warn("File '{}' already contains in '{}'", object.getName(), to);
      return false;
    }

    fsDirectory.getChildren().add(object);
    object.setParent(fsDirectory);
    return true;
  }

  public boolean removeFSObjectByPath(String path) {
    Optional<? extends FSObject> FSObjectOpt = getFSObjectByPath(path, FileSystem.ROOT);
    if (FSObjectOpt.isEmpty())
      return false;

    FSObject fsObject = FSObjectOpt.get();
    FSDirectory parent = fsObject.getParent();
    return parent.getChildren().remove(fsObject);
  }
}
