package service.controllers.api;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.entities.FSObject;
import java.util.Set;

public interface IFileSystemRestController {
  @PostMapping("/file")
  ResponseEntity<?> addNewFile(@RequestParam String path);

  @GetMapping("/files")
  ResponseEntity<Set<FSObject>> findFilesByPath(@RequestParam(defaultValue = "/") String path);

  @GetMapping("/file/download")
  ResponseEntity<FileSystemResource> findFileAndDownload(@RequestParam String path);

  @GetMapping("/file")
  ResponseEntity<?> findFileAndGetInfo(@RequestParam String path);

  @DeleteMapping("/file")
  ResponseEntity<?> delete(@RequestParam String path);
}
