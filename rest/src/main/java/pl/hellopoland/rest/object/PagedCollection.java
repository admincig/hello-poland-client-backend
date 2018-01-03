package pl.hellopoland.rest.object;

import java.util.Collection;

public class PagedCollection<T> {
  public Collection<T> items;

  public PagedCollection(Collection<T> items) {
    this.items = items;
  }
}
