package pl.hellopoland.util;

import java.util.Collection;
import pl.hellopoland.config.PagedCollectionConfig;
import pl.hellopoland.model.ModelSuperclass;

public class PagedEntityCollection<E extends ModelSuperclass> {
  public Collection<E> items;
  public PagedCollectionConfig<E> config;

  public PagedEntityCollection(Collection<E> items, PagedCollectionConfig<E> config) {
    this.items = items;
    this.config = config;
  }

}
