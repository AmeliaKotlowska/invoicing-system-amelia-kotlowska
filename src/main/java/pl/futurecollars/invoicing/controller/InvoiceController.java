package pl.futurecollars.invoicing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvoiceController {

  @GetMapping("/hello")
  public String helloWorld(@RequestParam(defaultValue = "Alan") String name){
    return String.format("Hello %s", name);
  }


}
