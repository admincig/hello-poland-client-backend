package pl.hellopoland.service.api.partner;

import org.apache.commons.lang3.tuple.Pair;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.rest.dto.LibraryFileORO;
import pl.hellopoland.service.LibraryFileService;
import pl.hellopoland.util.DtoMapper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class FileServicePartnerAPI {

  @Inject
  private LibraryFileService libraryFileService;

  public List<LibraryFileORO> uploadImages(List<Pair<String, byte[]>> pairs) {
    Partner partner = libraryFileService.getLoggedPartner();
    return pairs.stream()
        .map(pair -> libraryFileService.upload(pair.getLeft(), pair.getRight(), partner))
        .map(DtoMapper::getDTO)
        .collect(Collectors.toList());
  }
}
