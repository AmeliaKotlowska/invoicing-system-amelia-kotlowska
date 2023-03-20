package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Specification
import spock.lang.Stepwise

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.helpers.TestHelpers.invoice

@Stepwise
@AutoConfigureMockMvc
@SpringBootTest
class InvoiceControllerStepwiseTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    private Invoice originalInvoice = invoice(1)

    def 'empty array is returned when no invoices were created'() {
        when:
        def response = mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString
        then:
        response == "[]"
    }

    def 'add single invoice'() {
        given:
        def invoice = invoice(1)
        def invoiceAsJson = jsonService.convertToJson(invoice)

        when:
        def invoiceId = mockMvc.perform(post("/invoices")
                .content(invoiceAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        invoiceId == "1"
    }

    def 'one invoice is returned when getting all invoices'() {
        given:
        def expectedInvoice = originalInvoice
        expectedInvoice.id = 1

        when:
        def response = mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonService.convertToObject(response, Invoice[])

        then:
        invoices.size() == 1
        invoices[0] == expectedInvoice

    }

    def 'invoice is returned correctly when getting by id'() {

        given:
        def expectedInvoice = originalInvoice
        expectedInvoice.id = 1

        when:
        def response = mockMvc.perform(get("/invoices/1"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonService.convertToObject(response, Invoice)

        then:
        invoices == expectedInvoice
    }

    def 'update single invoice'() {
        given:
        def expectedInvoice = originalInvoice
        expectedInvoice.setDate(LocalDate.of(2025, 10, 10))
        def invoiceAsJson = jsonService.convertToJson(expectedInvoice)

        expect:
        mockMvc.perform(put("/invoices/1")
                .content(invoiceAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
    }

    def 'delete single invoice'() {
        expect:
        mockMvc.perform(delete("/invoices/1")).andExpect(status().isNoContent())

        and:
        mockMvc.perform(delete("/invoices/1")).andExpect(status().isNotFound())

        and:
        mockMvc.perform(get("/invoices/1")).andExpect(status().isNotFound())
    }
}
