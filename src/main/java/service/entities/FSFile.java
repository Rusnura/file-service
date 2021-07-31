package service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.io.InputStreamResource;

import java.nio.file.Path;

public class FSFile extends FSObject {
  private Path resource = null;

  public FSFile(String name) {
    super(name);
  }

  public FSFile(String name, Path resource) {
    super(name);
    this.resource = resource;
  }

  @JsonIgnore
  public Path getResource() {
    return resource;
  }

  public String getType() {
    return "file";
  }

  public void setResource(Path resource) {
    this.resource = resource;
  }
}
