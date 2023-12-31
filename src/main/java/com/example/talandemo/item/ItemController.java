package com.example.talandemo.item;

import static org.springframework.http.ResponseEntity.ok;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
public class ItemController {

  private final ItemService itemService;

  @Autowired
  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @PostMapping
  public ResponseEntity<Item> createItem(@RequestBody Item item) {
    Item createdItem = itemService.createItem(item);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item item) {
    Item updatedItem = itemService.updateItem(id, item);
    if (updatedItem != null) {
      return ok(updatedItem);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
    boolean deleted = itemService.deleteItem(id);
    if (deleted) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Item> getItemById(@PathVariable Long id) {
    Item item = itemService.getItemById(id);
    if (item != null) {
      return ok(item);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping
  public ResponseEntity<Page<Item>> getAllItems(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Item> items = itemService.getAllItems(pageable);
    return ResponseEntity.ok(items);
  }
}