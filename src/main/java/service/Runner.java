package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import service.components.FileSystem;
import service.entities.FSDirectory;
import service.entities.FSFile;

@SpringBootApplication
public class Runner extends SpringBootServletInitializer {
  @Autowired
  private FileSystem fileSystem;

  public static void main(String[] args) {
    SpringApplication.run(Runner.class, args);
  }
}