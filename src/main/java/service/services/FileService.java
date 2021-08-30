package service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.components.FileSystem;
import service.entities.FSObject;
import service.services.interfaces.IFileService;
import java.util.Optional;

@Service
public class FileService implements IFileService {
  @Autowired
  private FileSystem fileSystem;

  @Override
  public Optional<? extends FSObject> findFSObjectByPath(String path) {
    return fileSystem.getFSObjectByPath(path);
  }

  @Override
  public boolean addFSObjectToPath(FSObject fsObject, String to) {
    return fileSystem.addFSObjectToPath(fsObject, to);
  }

  @Override
  public boolean deleteFSObjectByPath(String path) {
    return fileSystem.removeFSObjectByPath(path);
  }
}
