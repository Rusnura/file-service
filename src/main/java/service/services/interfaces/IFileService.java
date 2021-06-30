package service.services.interfaces;

import service.entities.FSObject;

import java.util.Optional;

public interface IFileService {
  Optional<? extends FSObject> findFSObjectByPath(String path);
  boolean deleteFSObjectByPath(String path);
}
