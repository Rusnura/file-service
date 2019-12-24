package service.controllers;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.services.FileService;
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
}
