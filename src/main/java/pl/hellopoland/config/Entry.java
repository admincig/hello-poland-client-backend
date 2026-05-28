package pl.hellopoland.config;

import java.util.Map;

public class Entry {

  public Entry(String parameterName, Object value, String query) {
    this.parameterName = parameterName;
    this.value = value;
    this.query = query;
  }

  public Entry(Map<String, Object> parameters, String query) {
    this.parameters = parameters;
    this.query = query;
  }

  public String parameterName;
  public Object value;
  public Map<String, Object> parameters;
  private String query;

  @Override
  public String toString() {
    return query;
  }
}
