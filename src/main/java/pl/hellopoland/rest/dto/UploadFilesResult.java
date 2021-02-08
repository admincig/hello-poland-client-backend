package pl.hellopoland.rest.dto;

import pl.hellopoland.dto.FileDescriptorDTO;
import pl.hellopoland.dto.ImageDTO;

import java.util.ArrayList;
import java.util.List;

public class UploadFilesResult {

  public List<ImageDTO> images = new ArrayList<>();
  public List<FileDescriptorDTO> files = new ArrayList<>();

}
