package pl.futurecollars.invoicing.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.List;

public class JsonService {

  private final ObjectMapper objectMapper;

  {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  public String convertToJson(Object object){
    try {
      return objectMapper.writeValueAsString(List.of(object));
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to convert to JSON" + e);
    }
  }

  public <T> T convertToObject(String json, Class<T> clazz){
    try {
      return objectMapper.readValue(json, clazz);
    } catch (IOException e) {
      throw new RuntimeException("Failed to parse from JSON" + e);
    }
  }
}
