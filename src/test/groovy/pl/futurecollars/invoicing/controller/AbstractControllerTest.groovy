package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.service.TaxCalculatorResult
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.helpers.TestHelpers.invoice

@AutoConfigureMockMvc
@SpringBootTest
class AbstractControllerTest extends Specification {

    static final String INVOICE_ENDPOINT = "/invoices"
    static final String TAX_CALCULATOR_ENDPOINT = "/tax"

    @Autowired
    MockMvc mockMvc

    @Autowired
    JsonService jsonService

    def setup() {
        getAllInvoices().each { invoice -> deleteInvoice(invoice.id) }
    }

    int addInvoiceAndReturnId(Invoice invoice) {
        Integer.valueOf(
                mockMvc.perform(
                        post(INVOICE_ENDPOINT)
                                .content(jsonService.convertToJson(invoice))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                        .andExpect(status().isOk())
                        .andReturn()
                        .response
                        .contentAsString
        )
    }

    List<Invoice> getAllInvoices() {
        def response = mockMvc.perform(get(INVOICE_ENDPOINT))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        jsonService.convertToObject(response, Invoice[])
    }

    List<Invoice> addUniqueInvoices(int count) {
        (1..count).collect { id ->
            def invoice = invoice(id)
            invoice.id = addInvoiceAndReturnId(invoice)
            invoice
        }
    }

    void deleteInvoice(int id) {
        mockMvc.perform(delete("$INVOICE_ENDPOINT/$id"))
                .andExpect(status().isNoContent())
    }

    Invoice getInvoiceById(int id) {
        def invoiceAsString = mockMvc.perform(get("$INVOICE_ENDPOINT/$id"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        jsonService.convertToObject(invoiceAsString, Invoice)
    }

    String invoiceAsJson(int id) {
        jsonService.convertToJson(invoice(id))
    }

    TaxCalculatorResult calculateTax(Company company) {
        def response = mockMvc.perform(post("$TAX_CALCULATOR_ENDPOINT")
                .content(jsonService.convertToJson(company))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        jsonService.convertToObject(response, TaxCalculatorResult)
    }
}