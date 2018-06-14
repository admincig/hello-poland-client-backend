package pl.hellopoland.rest.dto;

import java.util.Collection;
import pl.hellopoland.config.PagedCollectionConfig;

public class PagedCollection {

  public PagedCollectionConfig<?> config;
  public Collection<?> items;


  public PagedCollection(Collection<?> items, PagedCollectionConfig<?> config) {
    this.items = items;
    this.config = config;
  }

}
