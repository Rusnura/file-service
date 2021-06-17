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
public class Runner extends SpringBootServletInitializer implements CommandLineRunner {
  @Autowired
  private FileSystem fileSystem;

  public static void main(String[] args) {
    SpringApplication.run(Runner.class, args);
  }

  @Override
  public void run(String... args) {
    fileSystem.addFSObjectToPath(new FSDirectory("bin"), "/");
    fileSystem.addFSObjectToPath(new FSDirectory("etc"), "/");
    fileSystem.addFSObjectToPath(new FSDirectory("home"), "/");
    fileSystem.addFSObjectToPath(new FSDirectory("rtu"), "/home");
    fileSystem.addFSObjectToPath(new FSDirectory("mp3"), "/home/rtu");
    fileSystem.addFSObjectToPath(new FSDirectory("jpg"), "/home/rtu");
    fileSystem.addFSObjectToPath(new FSFile("KAZKA - Плакала.mp3"), "/home/rtu/mp3");
    fileSystem.addFSObjectToPath(new FSFile("Captain Panic! - The Observer Redux Original Mix.mp3"), "/home/rtu/mp3");
    fileSystem.addFSObjectToPath(new FSFile("Legna Zeg - Brave New World.mp3"), "/home/rtu/mp3");
    fileSystem.addFSObjectToPath(new FSFile("Картинка 1.jpg"), "/home/rtu/jpg");
    fileSystem.addFSObjectToPath(new FSFile("Картинка 2.jpg"), "/home/rtu/jpg");
    fileSystem.addFSObjectToPath(new FSFile("Картинка 3.jpg"), "/home/rtu/jpg");
  }
}