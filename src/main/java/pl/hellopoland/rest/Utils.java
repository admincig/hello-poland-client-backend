package pl.hellopoland.rest;

import org.apache.commons.lang3.tuple.Pair;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {

  public static String getFileName(MultivaluedMap<String, String> header) {
    String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
    for (String filename : contentDisposition) {
      if ((filename.trim().startsWith("filename"))) {
        String[] name = filename.split("=");
        String finalFileName = name[1].trim().replaceAll("\"", "");
        return finalFileName;
      }
    }
    return "unknown";
  }

  public static List<Pair<String, byte[]>> extractFiles(MultipartFormDataInput input) throws
      IOException {
    List<Pair<String, byte[]>> pairs = new ArrayList<>();
    Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
    List<InputPart> inputParts = uploadForm.get("file");
    for (InputPart inputPart : inputParts) {
      MultivaluedMap<String, String> header = inputPart.getHeaders();
      String fileName = Utils.getFileName(header);
      byte[] bytes = inputPart.getBody(byte[].class, null);
      pairs.add(Pair.of(fileName, bytes));
    }
    return pairs;
  }

  public enum FileType {
    image, file
  }

}
