package service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import java.io.IOException;

public class File {
    private String name;
    private String extension;
    private long fileSize;
    private boolean isDirectory;
    private boolean isFile;
    private boolean isHidden;
    private boolean isReadable;
    private boolean isWritable;
    private java.io.File file;

    public File(@NotNull java.io.File file) {
        this.file = file;
        this.name = file.getName();
        this.isDirectory = file.isDirectory();
        this.isFile = file.isFile();
        if (isFile) {
        	this.extension = FilenameUtils.getExtension(file.getAbsolutePath());
        	this.fileSize = FileUtils.sizeOf(file);
        }
        this.isHidden = file.isHidden();
        this.isReadable = file.canRead();
        this.isWritable = file.canWrite();
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public long getFileSize() {
        return fileSize;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public boolean isFile() {
        return isFile;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public boolean isReadable() {
        return isReadable;
    }

    public boolean isWritable() {
        return isWritable;
    }

    @JsonIgnore
    public String getMimeType() throws IOException {
        return new Tika().detect(file);
    }

    @JsonIgnore
    public java.io.File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return "File{" +
                "name='" + name + '\'' +
                ", isDirectory=" + isDirectory +
                ", isHidden=" + isHidden +
                ", file=" + file +
                '}';
    }
}
