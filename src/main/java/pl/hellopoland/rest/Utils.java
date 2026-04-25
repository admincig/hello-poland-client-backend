package pl.hellopoland.rest;

import org.apache.commons.lang3.tuple.Pair;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import jakarta.ws.rs.core.MultivaluedMap;
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
    public static List<Pair<String, byte[]>> extractFiles(MultipartFormDataInput input) throws IOException {
        if (input == null) throw new IllegalArgumentException("Missing multipart body");

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        if (uploadForm == null || uploadForm.isEmpty()) {
            throw new IllegalArgumentException("Missing multipart form-data");
        }

        List<Pair<String, byte[]>> pairs = new ArrayList<>();

        for (Map.Entry<String, List<InputPart>> e : uploadForm.entrySet()) {
            List<InputPart> parts = e.getValue();
            if (parts == null) continue;

            for (InputPart part : parts) {
                MultivaluedMap<String, String> header = part.getHeaders();
                String cd = header.getFirst("Content-Disposition");

                // bierzemy tylko części, które wyglądają jak plik (mają filename=)
                if (cd == null || !cd.toLowerCase().contains("filename=")) continue;

                String fileName = Utils.getFileName(header);
                byte[] bytes = part.getBody(byte[].class, null);
                pairs.add(Pair.of(fileName, bytes));
            }
        }

        if (pairs.isEmpty()) {
            // pomocniczo: wypisz jakie klucze przyszły
            throw new IllegalArgumentException("No file part found in multipart. Keys=" + uploadForm.keySet());
        }

        return pairs;
    }

  public enum FileType {
    image, file
  }

}
