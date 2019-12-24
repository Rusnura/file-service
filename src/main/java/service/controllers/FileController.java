package service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.services.FileService;

@RestController
public class FileController {
    @Autowired
    private FileService fileService;

    @GetMapping("/files")
    public ResponseEntity<?> getFiles(@RequestParam String path, @RequestParam(defaultValue = "false") boolean showHidden) throws Exception {
        return ResponseEntity.ok(fileService.getFiles(path, showHidden));
    }
}
