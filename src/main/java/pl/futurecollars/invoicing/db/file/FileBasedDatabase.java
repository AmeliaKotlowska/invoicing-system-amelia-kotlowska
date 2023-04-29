package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.utils.FileService;
import pl.futurecollars.invoicing.utils.JsonService;

@AllArgsConstructor
public class FileBasedDatabase implements Database {

  private final Path databasePath;
  private final IdService idService;
  private final FileService fileService;
  private final JsonService jsonService;

  @Override
  public int save(Invoice invoice) {
    try {
      invoice.setId(idService.readLastIdFromFile());
      fileService.appendLineToFile(databasePath, jsonService.convertToJson(invoice));

      return invoice.getId();
    } catch (IOException ex) {
      throw new RuntimeException("Database failed to save invoice", ex);
    }
  }

  @Override
  public Optional<Invoice> getById(int id) {
    try {
      return fileService.readLines(databasePath)
          .stream()
          .filter(line -> containsId(line, id))
          .map(line -> jsonService.convertToObject(line, Invoice.class))
          .findFirst();
    } catch (IOException ex) {
      throw new RuntimeException("Database failed to get invoice with id: " + id, ex);
    }
  }

  @Override
  public List<Invoice> getAll() {
    try {
      return fileService.readLines(databasePath)
          .stream()
          .map(line -> jsonService.convertToObject(line, Invoice.class))
          .collect(Collectors.toList());
    } catch (IOException ex) {
      throw new RuntimeException("Failed to read invoices from file", ex);
    }
  }

  @Override
  public Optional<Invoice> update(int id, Invoice updatedInvoice) {
    try {
      List<String> allInvoices = fileService.readLines(databasePath);
      var invoicesWithoutInvoiceWithGivenId = allInvoices
          .stream()
          .filter(line -> !containsId(line, id))
          .collect(Collectors.toList());

      updatedInvoice.setId(id);
      invoicesWithoutInvoiceWithGivenId.add(jsonService.convertToJson(updatedInvoice));

      fileService.writeLinesToFile(databasePath, invoicesWithoutInvoiceWithGivenId);

      allInvoices.removeAll(invoicesWithoutInvoiceWithGivenId);
      return allInvoices.isEmpty() ? Optional.empty() : Optional.of(jsonService.convertToObject(allInvoices.get(0), Invoice.class));

    } catch (IOException ex) {
      throw new RuntimeException("Failed to update invoice with id: " + id, ex);
    }

  }

  @Override
  public Optional<Invoice> delete(int id) {
    try {
      var allInvoices = fileService.readLines(databasePath);
      var invoicesExceptDeleted = allInvoices
          .stream()
          .filter(line -> !containsId(line, id))
          .collect(Collectors.toList());

      fileService.writeLinesToFile(databasePath, invoicesExceptDeleted);

      allInvoices.removeAll(invoicesExceptDeleted);

      return allInvoices.isEmpty() ? Optional.empty() : Optional.of(jsonService.convertToObject(allInvoices.get(0), Invoice.class));

    } catch (IOException ex) {
      throw new RuntimeException("Failed to delete invoice with id: " + id, ex);
    }
  }

  private boolean containsId(String line, int id) {
    return line.contains("{\"id\":" + id + ",\"number\"");
  }
}
