package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class Runner extends SpringBootServletInitializer {
//public class Runner extends SpringBootServletInitializer implements CommandLineRunner {
  private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

  @Autowired
  private FileSystem fileSystem;

  @Autowired
  private FileSystemScanService scanService;

  public static void main(String[] args) {
    SpringApplication.run(Runner.class, args);
  }

//  @Override
//  public void run(String... args) throws Exception {
//    LOGGER.info("Starting of scan...");
//    scanService.scan(new File("/home/rtu/Documents"), "/");
//    LOGGER.info("Scan is completed!");
//  }
}