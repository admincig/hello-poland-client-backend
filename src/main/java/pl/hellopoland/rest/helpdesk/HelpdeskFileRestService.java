package pl.hellopoland.rest.helpdesk;


import org.apache.commons.lang3.tuple.Pair;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import pl.hellopoland.rest.Utils;
import pl.hellopoland.rest.dto.UploadFilesResult;
import pl.hellopoland.service.api.helpdesk.FileServiceHelpdeskAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@RequestScoped
@Path("/helpdesk/files")
@Produces(MediaType.APPLICATION_JSON)
public class HelpdeskFileRestService {

  @Inject
  private FileServiceHelpdeskAPI service;

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public UploadFilesResult uploadFiles(MultipartFormDataInput input,
      @QueryParam("partner") Long partnerId) throws IOException {
    List<Pair<String, byte[]>> pairs = Utils.extractFiles(input);
    return service.uploadFiles(pairs, partnerId);
  }

  @Path("/{fileId}")
  @DELETE
  public void delete(@PathParam("fileId") Long fileId, @QueryParam("type") Utils.FileType type,
      @QueryParam("partner") Long partnerId) {
    boolean isImage = Utils.FileType.image.equals(type);
    service.deleteFile(fileId, partnerId, isImage);
  }

}
