package service.components.filesystem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import service.components.FileSystem;
import service.entities.FSDirectory;
import service.entities.FSFile;
import service.entities.FSObject;

import java.util.Optional;

@SpringBootTest
public class FileSystemRemoveFSObjectByPath {
  @Autowired
  private FileSystem fileSystem;


  @Test
  public void removeFiles() {
    fileSystem.addFSObjectToPath(new FSFile("file1"), "/");

    fileSystem.removeFSObjectByPath("/file1");

    // Пробуем запросить несуществующий уже файл
    Optional<? extends FSObject> removedFile1 = fileSystem.getFSObjectByPath("/file1");

    // В теории он уже не должен существовать. isPresent() должен вернуть false, проверим
    Assertions.assertFalse(removedFile1.isPresent());
  }

  @Test
  public void removeDirectories() {
    // Создаём нужную структуру. Внутри /directory1 лежит файл file_in_dir1
    fileSystem.addFSObjectToPath(new FSDirectory("directory1"), "/");
    fileSystem.addFSObjectToPath(new FSFile("file_in_dir1"), "/directory1");

    // Внутрь директории можно положить ещё директорию
    fileSystem.addFSObjectToPath(new FSDirectory("subDirectory1"), "/directory1");
    fileSystem.addFSObjectToPath(new FSDirectory("subSubDirectory1"), "/directory1/subDirectory1");


    // Удаляем самую верхнюю директорию
    fileSystem.removeFSObjectByPath("/directory1");

    // Проверяем, что самая нижняя директория /directory1/subDirectory1/subSubDirectory1 тоже удалилась
    Optional<? extends FSObject> removedDir1 = fileSystem.getFSObjectByPath("/directory1/subDirectory1/subSubDirectory1");

    // Смотрим, что директория удалена
    Assertions.assertFalse(removedDir1.isPresent());

    // Пробуем запросить несуществующий уже файл в директории directory1
    Optional<? extends FSObject> removedFile1 = fileSystem.getFSObjectByPath("/directory1/file_in_dir1");

    // В теории он уже не должен существовать. isPresent() должен вернуть false, проверим
    Assertions.assertFalse(removedFile1.isPresent());
  }
}
