package pl.futurecollars.invoicing.controller.invoice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.futurecollars.invoicing.model.Invoice;

@RequestMapping(value = "invoices", produces = {"application/json;charset=UTF-8"})
@Api(tags = {"invoice controller"})
public interface InvoiceApi {

  @ApiOperation("Get list of all invoices")
  @GetMapping
  List<Invoice> getInvoices();

  @ApiOperation("Add new invoice to system")
  @PostMapping
  long addInvoice(@RequestBody Invoice invoice);

  @ApiOperation("Get invoice by id")
  @GetMapping(value = "/{id}")
  ResponseEntity<Invoice> getInvoiceById(@PathVariable int id);

  @ApiOperation("Delete invoice by id")
  @DeleteMapping("/{id}")
  ResponseEntity<?> deleteById(@PathVariable int id);

  @ApiOperation("Update invoice with given id to system")
  @PutMapping("{id}")
  ResponseEntity<?> updateById(@PathVariable int id, @RequestBody Invoice invoice);
}
