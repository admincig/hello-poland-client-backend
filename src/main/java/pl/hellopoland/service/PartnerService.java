package pl.hellopoland.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import pl.hellopoland.bo.Category;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.SightEventCategory;
import pl.hellopoland.config.PartnerPagedCollectionConfig;
import pl.hellopoland.util.Located;
import pl.hellopoland.util.PagedEntityCollection;

@LocalBean
@Stateless
public class PartnerService extends ServiceSuperclass {

  public Partner findByUserEmail(String email) {
    return em.createQuery(
        "select partner from User user join user.partner partner where lower(user.email) = :email",
        Partner.class).setParameter("email", email.toLowerCase()).getSingleResult();
  }

  public Partner findByToken(String token) {
    return em.createQuery("select partner from Partner partner where partner.hptToken=:token",
        Partner.class).setParameter("token", token).getSingleResult();
  }

  public List<Partner> getAll() {
    return em.createQuery("from Partner order by id asc", Partner.class).getResultList();
  }

  public PagedEntityCollection<Partner> getList(PartnerPagedCollectionConfig config) {
    List<Partner> list = getQuery(config).getResultList();
    return new PagedEntityCollection<>(list, config);
  }

  public Partner getPartnerWithCategoriesAndCities(Long id) {
    Partner partner = em.find(Partner.class, id);
    List<Category> categories =
        partner.getSight().stream().flatMap(sight -> sight.getSightEvents().stream())
            .flatMap(se -> se.getCategories().stream()).map(SightEventCategory::getCategory)
            .collect(Collectors.toList());
    partner.setCategories(categories);
    List<String> cities =
        Stream.<Located>concat(
            partner.getSight().stream(),
            partner.getSight().stream().flatMap(sight -> sight.getSightEvents().stream()))
            .map(se -> se.getLocation().getCity())
            .distinct()
            .collect(Collectors.toList());
    partner.setCities(cities);
    return partner;
  }
}
