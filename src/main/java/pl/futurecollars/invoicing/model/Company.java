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
public class Company {

  @ApiModelProperty(value = "Tax identification number", required = true, example = "111-222-333-44")
  private String taxIdentificationNumber;

  @ApiModelProperty(value = "Company address", required = true, example = "ul. Pogodna 12, 05-800 Pruszków")
  private String address;

  @ApiModelProperty(value = "Company name", required = true, example = "Invoicing sp z O.O.")
  private String name;

  @Builder.Default
  @ApiModelProperty(value = "Pension insurance amount", required = true, example = "1328.75")
  private BigDecimal pensionInsurance = BigDecimal.ZERO;

  @Builder.Default
  @ApiModelProperty(value = "Health insurance amount", required = true, example = "458.34")
  private BigDecimal healthInsurance = BigDecimal.ZERO;

}
