package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceEntry {

  @ApiModelProperty(value = "Product/service description", required = true, example = "Laptop Asus")
  private String description;

  @ApiModelProperty(value = "Number of items", required = true, example = "85")
  private int quantity;

  @ApiModelProperty(value = "Product netto price", required = true, example = "1234.56")
  private BigDecimal price;

  @ApiModelProperty(value = "Product tax value", required = true, example = "123.11")
  private BigDecimal vatValue;

  @ApiModelProperty(value = "Tax rate", required = true)
  private Vat vatRate;

}
