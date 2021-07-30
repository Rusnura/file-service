package service.controllers.api;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.entities.FSObject;
import java.util.Set;

public interface IFileSystemRestController {
  @GetMapping("/files")
  ResponseEntity<Set<FSObject>> findFilesByPath(@RequestParam(defaultValue = "/") String path);

  @GetMapping("/file")
  ResponseEntity<FileSystemResource> findFileAndDownload(@RequestParam String path);

  @DeleteMapping("/file")
  ResponseEntity<?> delete(@RequestParam String path);
}
