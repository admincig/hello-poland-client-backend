package pl.hellopoland.service.api.helpdesk;

import org.apache.commons.lang3.tuple.Pair;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.rest.dto.LibraryFileORO;
import pl.hellopoland.service.LibraryFileService;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.util.DtoMapper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class FileServiceHelpdeskAPI {

  @Inject
  private LibraryFileService service;
  @Inject
  private PartnerService partnerService;

  public List<LibraryFileORO> uploadImages(List<Pair<String, byte[]>> pairs, Long partnerId) {
    Partner partner = partnerId == null ? null : partnerService.get(partnerId);
    return pairs.stream()
        .map(pair -> service.upload(pair.getLeft(), pair.getRight(), partner))
        .map(DtoMapper::getDTO)
        .collect(Collectors.toList());
  }
}
