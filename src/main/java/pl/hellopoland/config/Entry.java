package pl.hellopoland.config;

public class Entry {

  public Entry(String parameterName, Object value, String query) {
    this.parameterName = parameterName;
    this.value = value;
    this.query = query;
  }

  public String parameterName;
  public Object value;
  private String query;

  @Override
  public String toString() {
    return query;
  }
}
