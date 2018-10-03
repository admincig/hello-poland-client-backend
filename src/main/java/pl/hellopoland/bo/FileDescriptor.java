package pl.hellopoland.bo;

import java.io.File;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import pl.hellopoland.exception.conflict.ConflictingException;

@Entity

public class FileDescriptor extends ModelSuperclass {

  private static final long serialVersionUID = 1L;

  public enum MimeType {
    PDF("application/pdf");
    private String type;

    private MimeType(String type) {
      this.type = type;
    }

    public String getType() {
      return this.type;
    }
  }

  @NotNull
  @Enumerated(EnumType.STRING)
  private MimeType mimeType;

  @NotNull
  private String path;

  @NotNull
  private LocalDateTime created;

  public FileDescriptor() {}

  public FileDescriptor(File file) {
    this.mimeType = guessMimeType(file);
    this.path = file.getPath();
    this.created = LocalDateTime.now();
  }

  public MimeType getMimeType() {
    return mimeType;
  }

  public void setMimeType(MimeType mimeType) {
    this.mimeType = mimeType;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public LocalDateTime getCreated() {
    return created;
  }

  public void setCreated(LocalDateTime created) {
    this.created = created;
  }

  private @NotNull MimeType guessMimeType(File file) {
    switch (getFileExtension(file)) {
      case "pdf":
        return MimeType.PDF;
      default:
        throw new ConflictingException("Not recognized file Mime type.");
    }
  }

  @Transient
  private String getFileExtension(File file) {
    String extension = "";
    try {
      if (file != null && file.exists()) {
        String name = file.getName();
        extension = name.substring(name.lastIndexOf(".") + 1);
      }
    } catch (Exception e) {
      extension = "";
    }
    return extension;
  }

}
