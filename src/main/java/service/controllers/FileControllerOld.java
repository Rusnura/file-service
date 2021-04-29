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
import service.models.FileEntityOld;
import service.services.FileServiceOld;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class FileControllerOld {
  private final FileServiceOld fileServiceOld;

  public FileControllerOld(FileServiceOld fileServiceOld) {
    this.fileServiceOld = fileServiceOld;
  }

  @GetMapping("/files")
  public ResponseEntity<?> getFiles(@RequestParam(defaultValue = "/") String path,
                                    @RequestParam(defaultValue = "") String password,
                                    @RequestParam(defaultValue = "false") boolean showHidden) throws Exception {
    return ResponseEntity.ok(fileServiceOld.getFiles(path, password, showHidden));
  }

  @GetMapping("/file")
  public void getFile(HttpServletResponse response,
                      @RequestParam String path,
                      @RequestParam(defaultValue = "") String password) throws IOException {
    FileEntityOld file = fileServiceOld.getFile(path, password);
    response.setStatus(HttpServletResponse.SC_OK);
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());
    response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.getFileSize()));
    response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.asMediaType(MimeType.valueOf(file.getMimeType())).toString());
    try (FileInputStream fis = new FileInputStream(file)) {
      IOUtils.copy(fis, response.getOutputStream());
    }
  }

  @PostMapping(value = "/file", consumes = "multipart/form-data")
  public ResponseEntity<FileEntityOld> createFile(@RequestPart @Valid @NotNull @NotBlank MultipartFile file,
                                                  @RequestParam String path,
                                                  @RequestParam(defaultValue = "") String password,
                                                  @RequestParam(defaultValue = "false") boolean override,
                                                  @RequestParam(defaultValue = "false") boolean createParentFolders) throws IOException {
    try {
      return ResponseEntity.ok(fileServiceOld.putFile(path, password, file, override, createParentFolders));
    } catch (FileExistsException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  @DeleteMapping(value = "/file")
  public ResponseEntity<?> deleteFile(@RequestParam String path,
                                      @RequestParam(defaultValue = "") String password) throws IOException {
    return (fileServiceOld.deleteFile(path, password) ? ResponseEntity.ok() : ResponseEntity.badRequest()).build();
  }
}
