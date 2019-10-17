package pl.hellopoland.service.api.partner;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Tag;
import pl.hellopoland.config.TagPagedCollectionConfig;
import pl.hellopoland.dto.TagDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.TagService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;

@Stateless
public class TagServicePartnerAPI {

  @Inject
  TagService service;
  @Inject
  TranslationService tService;

  @RolesAllowed("partner")
  public PagedCollection pagedList(LanguageVersion language) {
    var config = new TagPagedCollectionConfig();
    var bos = service.pagedList(config);
    bos.items = tService.translateEntities(bos.items, language);
    List<TagDTO> dtos = bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    return new PagedCollection(dtos, bos.config);
  }

  @RolesAllowed("partner")
  public TagDTO get(Long id, LanguageVersion language) {
    Tag tag = service.get(id);
    tag = tService.translateEntity(tag, language);
    return DtoMapper.getFullDTO(tag);
  }

}
