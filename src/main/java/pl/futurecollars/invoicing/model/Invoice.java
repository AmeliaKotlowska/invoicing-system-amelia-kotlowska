package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

  @ApiModelProperty(value = "Invoice Id (generated by application)", required = true, example = "1")
  private int id;

  @ApiModelProperty(value = "Date when invoice was created", required = true)
  private LocalDate date;

  @ApiModelProperty(value = "Company who bought the product", required = true)
  private Company buyer;

  @ApiModelProperty(value = "Company who sell this product", required = true)
  private Company seller;

  @ApiModelProperty(value = "List of products", required = true)
  private List<InvoiceEntry> entries;

}
