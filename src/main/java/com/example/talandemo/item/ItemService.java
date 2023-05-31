package com.example.talandemo.item;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

  private final ItemRepository itemRepository;

  @Autowired
  public ItemService(ItemRepository itemRepository) {
    this.itemRepository = itemRepository;
  }

  public Item createItem(Item item) {
    return itemRepository.save(item);
  }


  public Item updateItem(Long id, Item updatedItem) {
    Optional<Item> optionalItem = itemRepository.findById(id);
    if (optionalItem.isPresent()) {
      Item existingItem = optionalItem.get();
      existingItem.setName(updatedItem.getName());
      existingItem.setDescription(updatedItem.getDescription());
      // Update other fields as needed
      return itemRepository.save(existingItem);
    } else {
      return null;
    }
  }

  public boolean deleteItem(Long id) {
    Optional<Item> optionalItem = itemRepository.findById(id);
    if (optionalItem.isPresent()) {
      itemRepository.delete(optionalItem.get());
      return true;
    } else {
      return false;
    }
  }

  public Item getItemById(Long id) {
    Optional<Item> optionalItem = itemRepository.findById(id);
    return optionalItem.orElse(null);
  }

  public Page<Item> getAllItems(Pageable pageable) {
    return itemRepository.findAll(pageable);
  }
}
