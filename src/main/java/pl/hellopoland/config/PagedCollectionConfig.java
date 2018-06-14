package pl.hellopoland.config;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.LinkedList;
import pl.hellopoland.ModelSuperclass;

public abstract class PagedCollectionConfig<E extends ModelSuperclass> {

  private Collection<Entry> conditions;
  private Integer pageSize;
  private Integer pageNum = 0;
  private String orderColumn = "id";
  private String orderDirection = "desc";

  protected void addCondition(String parameterName, Object value, String query) {
    if (conditions == null) {
      conditions = new LinkedList<>();
    }
    conditions.add(new Entry(parameterName, value, query));
  }

  @SuppressWarnings("unchecked")
  public Class<E> entityClass() {
    return (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass())
        .getActualTypeArguments()[0];
  }

  public String joins() {
    return "";
  }

  public String getOrder() {
    return orderColumn + " " + orderDirection;
  }

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

  public Collection<Entry> getConditions() {
    return conditions;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getPageNum() {
    return pageNum;
  }

  public void setPageNum(Integer pageNum) {
    this.pageNum = pageNum;
  }

  public void setOrderColumn(String orderColumn) {
    this.orderColumn = orderColumn;
  }

  public void setOrderDirection(String orderDirection) {
    this.orderDirection = orderDirection;
  }

}

