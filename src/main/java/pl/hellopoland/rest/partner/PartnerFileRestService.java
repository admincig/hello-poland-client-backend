package pl.hellopoland.rest.partner;

import org.apache.commons.lang3.tuple.Pair;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import pl.hellopoland.rest.Utils;
import pl.hellopoland.rest.dto.LibraryFileORO;
import pl.hellopoland.service.api.partner.FileServicePartnerAPI;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@RequestScoped
@Path("/partner/files")
@Produces(MediaType.APPLICATION_JSON)
public class PartnerFileRestService {

  @Inject
  private FileServicePartnerAPI service;

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public List<LibraryFileORO> uploadFiles(MultipartFormDataInput input) throws IOException {
    List<Pair<String, byte[]>> pairs = Utils.extractFiles(input);
    return service.uploadImages(pairs);
  }

}
