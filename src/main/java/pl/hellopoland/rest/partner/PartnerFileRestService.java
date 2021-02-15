package pl.hellopoland.rest.partner;

import org.apache.commons.lang3.tuple.Pair;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import pl.hellopoland.rest.Utils;
import pl.hellopoland.rest.dto.UploadFilesResult;
import pl.hellopoland.service.api.partner.FileServicePartnerAPI;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
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
  public UploadFilesResult uploadFiles(MultipartFormDataInput input) throws IOException {
    List<Pair<String, byte[]>> pairs = Utils.extractFiles(input);
    return service.uploadFiles(pairs);
  }

  @Path("/{fileId}")
  @DELETE
  public void delete(@PathParam("fileId") Long fileId, @QueryParam("type") Utils.FileType type) {
    boolean isImage = Utils.FileType.image.equals(type);
    service.deleteFile(fileId, isImage);
  }

}
