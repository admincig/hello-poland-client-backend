package pl.hellopoland.rest.dto;

import pl.hellopoland.config.PagedCollectionConfig;

import java.util.Collection;

public class PagedCollection<T> {

  public PagedCollectionConfig<?> config;
  public Collection<T> items;


  public PagedCollection(Collection<T> items, PagedCollectionConfig<?> config) {
    this.items = items;
    this.config = config;
  }

}
