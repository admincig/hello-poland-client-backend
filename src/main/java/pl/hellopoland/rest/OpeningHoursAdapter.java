package pl.hellopoland.rest;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.bind.adapter.JsonbAdapter;
import pl.hellopoland.dto.OpeningHoursDTO;

public class OpeningHoursAdapter implements JsonbAdapter<OpeningHoursDTO, JsonObject> {
  @Override
  public JsonObject adaptToJson(OpeningHoursDTO dto) throws Exception {


    // JsonbBuilder.create().toJson(LocalTime.parse(dto.closeTime.toString()));



    JsonObjectBuilder job = Json.createObjectBuilder().add("closeTime", dto.closeTime.toString())
        .add("day", dto.day).add("openTime", dto.openTime.toString());
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

  @Override
  public OpeningHoursDTO adaptFromJson(JsonObject adapted) throws Exception {
    var oHours = new OpeningHoursDTO();
    // if (adapted.containsKey("id")) {
    // oHours.setId(Long.valueOf(adapted.getInt("id")));
    // }
    oHours.closeTime =
        LocalTime.parse(adapted.getString("closeTime"), DateTimeFormatter.ofPattern("HH:mm"));
    oHours.openTime =
        LocalTime.parse(adapted.getString("openTime"), DateTimeFormatter.ofPattern("HH:mm"));
    oHours.day = adapted.getInt("day");
    return oHours;
  }
}
