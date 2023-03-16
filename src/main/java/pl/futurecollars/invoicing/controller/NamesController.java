package pl.futurecollars.invoicing.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Name;

@RestController
@RequestMapping("names")
public class NamesController {
//robienie mapy z imionami
  private Map<Integer, String> names = new  HashMap<>();
  private int nextId = 1;

  @GetMapping
  public List<Name> getAllNames(){
    return names.entrySet().stream()
        .map(entry -> new Name(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }
  @PostMapping
  public int addName(@RequestBody String name){
    names.put(nextId,name);
    return nextId++;
  }

  @GetMapping("/{id}")//id użyte w środku
  public ResponseEntity<Name> getSingleNameById(@PathVariable int id){
    return Optional.ofNullable(names.get(id))
        .map(name -> new Name(id, name))
        .map(name -> ResponseEntity.ok().body(name))
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable int id){
    return Optional.ofNullable(names.remove(id))
        .map(name ->ResponseEntity.noContent().build())
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("{id}")
  public ResponseEntity<?> updateById(@PathVariable int id, @RequestBody Name nameRq){
    return Optional.ofNullable(names.put(id, nameRq.getName()))
        .map(name -> ResponseEntity.noContent().build())
        .orElse(ResponseEntity.notFound().build());
  }
}
