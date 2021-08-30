package service.services.interfaces;

import service.entities.FSFile;
import service.entities.FSObject;

import java.util.Optional;

public interface IFileService {
  Optional<? extends FSObject> findFSObjectByPath(String path);
  boolean addFSObjectToPath(FSObject fsObject, String to);
  boolean deleteFSObjectByPath(String path);
}
