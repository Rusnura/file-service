package service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;

public class FileEntity extends File {
  private String password = null;

//  public FileEntity(String pathname) {
//    super(pathname);
//  }

  public FileEntity(String pathname, String password) {
    super(pathname);
    this.password = password;
  }

  public String getExtension() {
    return FilenameUtils.getExtension(this.getAbsolutePath());
  }

  @JsonIgnore
  public String getMimeType() throws IOException {
    return new Tika().detect(this);
  }

  @JsonIgnore
  public String getPassword() {
    return password;
  }
}
