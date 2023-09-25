package pl.futurecollars.invoicing.db.file;

import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.helpers.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.utils.FileService
import pl.futurecollars.invoicing.utils.JsonService

import java.nio.file.Files
import java.nio.file.Path

class FileBasedDatabaseTest extends AbstractDatabaseTest {

    Path dbFilePath

    @Override
    Database getDatabaseInstance() {
        def fileService = new FileService()

        def idPath = File.createTempFile('ids', '.txt').toPath()
        def idService = new IdService(idPath, fileService)

        dbFilePath = File.createTempFile('invoices', '.txt').toPath()
        new FileBasedDatabase(dbFilePath, idService, fileService, new JsonService(), Invoice)
    }

    def "file based database writes invoices to correct file"() {
        given:
        def db = getDatabaseInstance()

        when:
        db.save(TestHelpers.invoice(4))

        then:
        1 == Files.readAllLines(dbFilePath).size()

        when:
        db.save(TestHelpers.invoice(5))

        then:
        2 == Files.readAllLines(dbFilePath).size()
    }
}