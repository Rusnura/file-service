package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import service.components.FileSystem;
import service.entities.FSDirectory;
import service.entities.FSObject;

import java.util.Optional;

@SpringBootApplication
public class Runner extends SpringBootServletInitializer implements CommandLineRunner {
  @Autowired
  private FileSystem fileSystem;

  public static void main(String[] args) {
    SpringApplication.run(Runner.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    FSDirectory test1 = new FSDirectory("test1");
    FSDirectory subtest1 = new FSDirectory("subtest1");
    FSDirectory subSubtest1 = new FSDirectory("subSubtest1");

    subtest1.getChildren().add(subSubtest1);
    test1.getChildren().add(subtest1);
    FileSystem.root.getChildren().add(test1);

    Optional<? extends FSObject> object = fileSystem.getFileByPath("test1/subtest1/subSubtest1/test", FileSystem.root);
  }
}