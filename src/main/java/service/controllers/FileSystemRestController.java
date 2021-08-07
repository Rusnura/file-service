package service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import service.controllers.api.IFileSystemRestController;
import service.entities.FSDirectory;
import service.entities.FSFile;
import service.entities.FSObject;
import service.services.interfaces.IFileService;
import java.io.File;
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
  public ResponseEntity<FileSystemResource> findFileAndDownload(String path) {
    Optional<? extends FSObject> fsObjectOpt = fileService.findFSObjectByPath(path);
    if (fsObjectOpt.isEmpty())
      return ResponseEntity.notFound().build();

    FSObject fsObject = fsObjectOpt.get();
    if (!(fsObject instanceof FSFile))
      return ResponseEntity.unprocessableEntity().build();

    FSFile virtualFile = (FSFile) fsObject;
    File realFile = virtualFile.getResource().toFile();
    if (!realFile.exists() || !realFile.canRead() || !realFile.isFile())
      return ResponseEntity.unprocessableEntity().build();

    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
    headers.add("Content-Disposition", "attachment; filename=\"" + virtualFile.getName() + "\"");
    return new ResponseEntity<>(new FileSystemResource(realFile), headers, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<?> findFileAndGetInfo(String path) {
    Optional<? extends FSObject> fsObjectOpt = fileService.findFSObjectByPath(path);
    if (fsObjectOpt.isEmpty())
      return ResponseEntity.notFound().build();
    return ResponseEntity.ok(fsObjectOpt.get());
  }

  @Override
  public ResponseEntity<?> delete(String path) {
    boolean result = fileService.deleteFSObjectByPath(path);
    return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
