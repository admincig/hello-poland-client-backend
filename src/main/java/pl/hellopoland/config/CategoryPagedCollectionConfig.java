package pl.hellopoland.config;

import pl.hellopoland.bo.Category;

public class CategoryPagedCollectionConfig extends PagedCollectionConfig<Category> {

  @Override
  public String getOrder() {
    return "case when e.displayOrder is null then 1 else 0 end, e.displayOrder asc, e.id desc";
  }

}
