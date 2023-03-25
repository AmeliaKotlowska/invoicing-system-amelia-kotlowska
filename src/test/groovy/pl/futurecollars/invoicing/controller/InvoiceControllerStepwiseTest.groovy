package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Shared
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

    private LocalDate updatedDate = LocalDate.of(2020, 02, 28)

    @Shared
    private int invoiceId

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
        def invoiceAsJson = jsonService.convertToJson(originalInvoice)

        when:
        def result = mockMvc.perform(post("/invoices")
                .content(invoiceAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        invoiceId = Integer.valueOf(result)

        then:
        invoiceId > 0
    }

    def 'one invoice is returned when getting all invoices'() {
        given:
        def expectedInvoice = originalInvoice
        expectedInvoice.id = invoiceId

        when:
        def response = mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoice = jsonService.convertToObject(response, Invoice[])

        then:
        invoice.size() == 1
        invoice[0] == expectedInvoice

    }

    def 'invoice is returned correctly when getting by id'() {

        given:
        def expectedInvoice = originalInvoice
        expectedInvoice.id = invoiceId

        when:
        def response = mockMvc.perform(get("/invoices/$invoiceId"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoice = jsonService.convertToObject(response, Invoice)

        then:
        invoice == expectedInvoice
    }

    def 'update single invoice'() {
        given:
        def expectedInvoice = originalInvoice
        expectedInvoice.date = updatedDate

        def invoiceAsJson = jsonService.convertToJson(expectedInvoice)

        expect:
        mockMvc.perform(put("/invoices/$invoiceId")
                .content(invoiceAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
    }

    def 'updated invoice is returned correctly'() {
        given:
        def expectedInvoice = originalInvoice
        expectedInvoice.id = invoiceId
        expectedInvoice.date = updatedDate

        when:
        def response = mockMvc.perform(get("/invoices/$invoiceId"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonService.convertToObject(response, Invoice)

        then:
        invoices == expectedInvoice
    }


    def 'delete single invoice'() {
        expect:
        mockMvc.perform(delete("/invoices/$invoiceId")).andExpect(status().isNoContent())

        and:
        mockMvc.perform(delete("/invoices/$invoiceId")).andExpect(status().isNotFound())

        and:
        mockMvc.perform(get("/invoices/$invoiceId")).andExpect(status().isNotFound())
    }
}
