package service.controllers;

import com.sun.istack.NotNull;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import service.services.FileService;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.FileInputStream;
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
    public void getFile(HttpServletResponse response, @RequestParam String path) throws IOException {
		service.models.File file = fileService.getFile(path);
		response.setStatus(HttpServletResponse.SC_OK);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());
		response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.getFileSize()));
		response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.asMediaType(MimeType.valueOf(file.getMimeType())).toString());
		IOUtils.copy(new FileInputStream(file.getFile()), response.getOutputStream());
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

    @DeleteMapping(value = "/file")
    public ResponseEntity<?> deleteFile(@RequestParam String path) throws IOException {
        return (fileService.deleteFile(path) ? ResponseEntity.ok() : ResponseEntity.badRequest()).build();
    }
}
