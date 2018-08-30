package pl.hellopoland.rest;

import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.adapter.JsonbAdapter;
import pl.hellopoland.dto.SightDTO;

public class SightAdapter implements JsonbAdapter<SightDTO, JsonObject> {
  @Override
  public JsonObject adaptToJson(SightDTO dto) throws Exception {



    var arrayBuilder = Json.createArrayBuilder(dto.openingHours).build();



    JsonObjectBuilder job =
        Json.createObjectBuilder().add("openingHours", arrayBuilder).add("name", dto.name);
    // .add("openingHours", JsonbBuilder.create().toJson(dto.openingHours)).add("name", dto.name);



    // if (oHours.getId() != null) {
    // job.add("id", oHours.getId());
    // }

    // TODO:!!!!!!!!!!!
    // if (oHours.getSight() != null) {
    // job.add("sight", JsonbConfig.getInstance().toJson(oHours.getSight()));
    // }
    // if (oHours.getSightEvent() != null) {
    // job.add("sight", JsonbConfig.getInstance().toJson(oHours.getSightEvent()));
    // }
    // !!!!!!!


    return job.build();
  }

  @SuppressWarnings("unchecked")
  @Override
  public SightDTO adaptFromJson(JsonObject adapted) throws Exception {
    var dto = new SightDTO();
    dto.openingHours =
        JsonbBuilder.create().fromJson(adapted.getString("openingHours"), List.class);
    dto.name = adapted.getString("name");
    return dto;
  }
}
