package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Path;
import pl.futurecollars.invoicing.utils.FileService;


public class IdService {

  private int lastId;

  private final Path idFilePath;

  private final FileService fileService;

  public IdService(Path idFilePath, FileService fileService) {
    this.idFilePath = idFilePath;
    this.fileService = fileService;
    this.lastId = this.readLastIdFromFile();
  }

  public int getLastUsedId() {
    return lastId;
  }

  public void saveLastUsedIdToFile() {
    try {
      fileService.writeLineToFile(this.idFilePath, String.valueOf(lastId++));
    } catch (IOException e) {
      throw new RuntimeException("Error saving last used ID to file: " + e);
    }
  }

  public int readLastIdFromFile() {
    try {
      return Integer.parseInt(fileService.readLines(idFilePath).toString());
    } catch (IOException e) {
      throw new RuntimeException("Error reading last used ID from file: " + e);
    }
  }
}
