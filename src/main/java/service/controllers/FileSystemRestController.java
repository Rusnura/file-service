package service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import service.controllers.api.IFileSystemRestController;
import service.entities.FSDirectory;
import service.entities.FSObject;
import service.services.interfaces.IFileService;
import java.util.Optional;
import java.util.Set;

@RestController
public class FileSystemRestController implements IFileSystemRestController {
  @Autowired
  private IFileService fileService;

  @Override
  public ResponseEntity<Set<FSObject>> findFilesByPath(String path) {
    Optional<? extends FSObject> fsObjectOpt = fileService.findFSObjectByPath(path);
    if (fsObjectOpt.isEmpty())
      return ResponseEntity.notFound().build();

    FSObject fsObject = fsObjectOpt.get();
    if (!(fsObject instanceof FSDirectory))
      return ResponseEntity.unprocessableEntity().build();

    return ResponseEntity.ok(((FSDirectory) fsObject).getChildren());
  }

  @Override
  public ResponseEntity<?> delete(String path) {
    boolean result = fileService.deleteFSObjectByPath(path);
    return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
