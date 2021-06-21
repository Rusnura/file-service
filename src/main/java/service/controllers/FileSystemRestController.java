package service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import service.components.FileSystem;
import service.controllers.api.IFileSystemRestController;
import service.entities.FSDirectory;
import service.entities.FSObject;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class FileSystemRestController implements IFileSystemRestController {
  @Autowired
  private FileSystem fileSystem;

  @Override
  public ResponseEntity<Set<FSObject>> findFilesByPath(String path) {
    Optional<? extends FSObject> fsObjectOpt = fileSystem.getFSObjectByPath(path);
    if (fsObjectOpt.isEmpty())
      return ResponseEntity.notFound().build();

    FSObject fsObject = fsObjectOpt.get();
    if (!(fsObject instanceof FSDirectory))
      return ResponseEntity.unprocessableEntity().build();

    return ResponseEntity.ok(((FSDirectory) fsObject).getChildren());
  }
}
