package pl.futurecollars.invoicing.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import pl.futurecollars.invoicing.utils.FileService;


public class IdService extends FileService {

  private int lastId;

  private String idFilePath;

  public IdService(int lastId, String idFilePath) {
    this.lastId = lastId;
    this.idFilePath = idFilePath;
  }

  public int getLastUsedId() {
    return lastId;
  }

  public void save(Object object) throws IOException {
    super.writeLineToFile(object, lastId);
    lastId++;
    saveLastUsedIdToFile();
  }

  private int readLastIdFromFile() {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(idFilePath));
      int id = Integer.parseInt(reader.readLine());
      reader.close();
      return id;
    } catch (IOException e) {
      System.err.println("Error reading last used ID from file: " + e);
      return 0;
    }
  }

  private void saveLastUsedIdToFile() {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(idFilePath));
      writer.write(Integer.toString(lastId));
      writer.close();
    } catch (IOException e) {
      System.err.println("Error saving last used ID to file: " + e);
    }
  }

}
