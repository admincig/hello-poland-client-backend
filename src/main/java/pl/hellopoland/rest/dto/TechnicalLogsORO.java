package pl.hellopoland.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class TechnicalLogsORO {

  public String source;
  public String fetchedAt;
  public List<String> lines = new ArrayList<>();
}
