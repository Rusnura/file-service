package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import service.components.FileSystem;
import service.entities.FSDirectory;
import service.entities.FSFile;
import service.services.FileSystemScanService;
import java.io.File;

@SpringBootApplication
public class Runner extends SpringBootServletInitializer /*implements CommandLineRunner*/ {
  @Autowired
  private FileSystem fileSystem;

//  @Autowired
//  private FileSystemScanService scanService;

  public static void main(String[] args) {
    SpringApplication.run(Runner.class, args);
  }

//  @Override
//  public void run(String... args) throws Exception {
//    scanService.scan(new File("/home/rtu/Documents"), "/");
//  }
}