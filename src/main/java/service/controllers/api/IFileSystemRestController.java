package service.controllers.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.entities.FSObject;
import java.util.List;

public interface IFileSystemRestController {
  @GetMapping("/files")
  ResponseEntity<List<FSObject>> findFilesByPath(@RequestParam(defaultValue = "/") String path);
}
