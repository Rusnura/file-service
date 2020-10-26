package service.controllers;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.services.FileService;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class FileController {
  private final FileService fileService;

  public FileController(FileService fileService) {
    this.fileService = fileService;
  }

  @GetMapping("/files")
  public ResponseEntity<?> getFiles(@RequestParam(defaultValue = "/") String path, @RequestParam(defaultValue = "false") boolean showHidden) throws Exception {
    return ResponseEntity.ok(fileService.getFiles(path, showHidden));
  }

  @GetMapping("/file")
  public void getFile(HttpServletResponse response, @RequestParam String path) throws IOException {
    service.models.File file = fileService.getFile(path);
    response.setStatus(HttpServletResponse.SC_OK);
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());
    response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.getFileSize()));
    response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.asMediaType(MimeType.valueOf(file.getMimeType())).toString());
    try (FileInputStream fis = new FileInputStream(file.getFile())) {
      IOUtils.copy(fis, response.getOutputStream());
    }
  }

  @PostMapping(value = "/file", consumes = "multipart/form-data")
  public ResponseEntity<service.models.File> create(@RequestPart @Valid @NotNull @NotBlank MultipartFile file,
                                                    @RequestParam String path,
                                                    @RequestParam(defaultValue = "false") boolean override,
                                                    @RequestParam(defaultValue = "false") boolean createParentFolders) throws IOException {
    try {
      return ResponseEntity.ok(fileService.putFile(path, file, override, createParentFolders));
    } catch (FileExistsException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  @DeleteMapping(value = "/file")
  public ResponseEntity<?> deleteFile(@RequestParam String path) throws IOException {
    return (fileService.deleteFile(path) ? ResponseEntity.ok() : ResponseEntity.badRequest()).build();
  }
}
