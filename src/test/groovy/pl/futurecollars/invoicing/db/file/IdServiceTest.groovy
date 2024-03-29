package pl.futurecollars.invoicing.db.file

import pl.futurecollars.invoicing.utils.FileService;
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path;

class IdServiceTest extends Specification {
  private final Path nextIdDbPath = File.createTempFile('nextId', '.txt').toPath()

  def "next id starts from 1 if file was empty"() {
    given:
    IdService idService = new IdService(nextIdDbPath, new FileService())

    expect:
        ['1'] == Files.readAllLines(nextIdDbPath)

    and:
    1 == idService.readLastIdFromFile()
        ['2'] == Files.readAllLines(nextIdDbPath)

    and:
    2 == idService.readLastIdFromFile()
        ['3'] == Files.readAllLines(nextIdDbPath)

    and:
    3 == idService.readLastIdFromFile()
        ['4'] == Files.readAllLines(nextIdDbPath)
  }

  def "next id starts from last number if file was not empty"() {
    given:
    Files.writeString(nextIdDbPath, "17")
    IdService idService = new IdService(nextIdDbPath, new FileService())

    expect:
        ['17'] == Files.readAllLines(nextIdDbPath)

    and:
    17 == idService.readLastIdFromFile()
        ['18'] == Files.readAllLines(nextIdDbPath)

    and:
    18 == idService.readLastIdFromFile()
        ['19'] == Files.readAllLines(nextIdDbPath)

    and:
    19 == idService.readLastIdFromFile()
        ['20'] == Files.readAllLines(nextIdDbPath)
  }
}