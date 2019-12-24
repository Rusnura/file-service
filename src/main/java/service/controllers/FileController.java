package service.controllers;

import com.sun.istack.NotNull;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import service.services.FileService;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

@RestController
public class FileController {
    @Autowired
    private FileService fileService;

    @GetMapping("/files")
    public ResponseEntity<?> getFiles(@RequestParam String path, @RequestParam(defaultValue = "false") boolean showHidden) throws Exception {
        return ResponseEntity.ok(fileService.getFiles(path, showHidden));
    }

    @GetMapping("/file")
    public ResponseEntity<?> getFile(@RequestParam String path) throws IOException {
        service.models.File file = fileService.getFile(path);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
            .contentLength(file.getFileSize())
            .contentType(MediaType.asMediaType(MimeType.valueOf(file.getMimeType())))
            .body(new ByteArrayResource(FileUtils.readFileToByteArray(file.getFile())));
    }

    @PostMapping(value = "/file", consumes = "multipart/form-data")
    public ResponseEntity<service.models.File> create(@RequestPart @Valid @NotNull @NotBlank MultipartFile file,
                                                      @RequestParam String path,
                                                      @RequestParam(defaultValue = "false") boolean override) throws IOException {
        try {
            return ResponseEntity.ok(fileService.putFile(path, file, override));
        } catch (FileExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
