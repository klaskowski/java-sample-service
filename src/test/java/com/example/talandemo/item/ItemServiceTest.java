package com.example.talandemo.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ItemServiceTest {


  @Mock
  private ItemRepository itemRepository;

  @InjectMocks
  private ItemService itemService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void createItem_ReturnsNewItem() {
    // Arrange
    Item item = new Item();
    item.setName("Test Item");
    item.setDescription("This is a test item");

    when(itemRepository.save(any(Item.class))).thenReturn(item);

    // Act
    Item createdItem = itemService.createItem(item);

    // Assert
    assertNotNull(createdItem);
    assertEquals("Test Item", createdItem.getName());
    assertEquals("This is a test item", createdItem.getDescription());
    verify(itemRepository, times(1)).save(any(Item.class));
  }

  @Test
  void updateItem_WithValidId_ReturnsUpdatedItem() {
    // Arrange
    Long itemId = 1L;
    Item existingItem = new Item();
    existingItem.setId(itemId);
    existingItem.setName("Existing Item");
    existingItem.setDescription("This is an existing item");

    Item updatedItem = new Item();
    updatedItem.setId(itemId);
    updatedItem.setName("Updated Item");
    updatedItem.setDescription("This is the updated item");

    when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));
    when(itemRepository.save(any(Item.class))).thenReturn(updatedItem);

    // Act
    Item result = itemService.updateItem(itemId, updatedItem);

    // Assert
    assertNotNull(result);
    assertEquals(itemId, result.getId());
    assertEquals("Updated Item", result.getName());
    assertEquals("This is the updated item", result.getDescription());
    verify(itemRepository, times(1)).findById(itemId);
    verify(itemRepository, times(1)).save(any(Item.class));
  }

  @Test
  void updateItem_WithInvalidId_ReturnsNull() {
    // Arrange
    Long itemId = 1L;
    Item updatedItem = new Item();
    updatedItem.setName("Updated Item");
    updatedItem.setDescription("This is the updated item");

    when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

    // Act
    Item result = itemService.updateItem(itemId, updatedItem);

    // Assert
    assertNull(result);
    verify(itemRepository, times(1)).findById(itemId);
    verify(itemRepository, never()).save(any(Item.class));
  }

  @Test
  void deleteItem_WithValidId_ReturnsTrue() {
    // Arrange
    Long itemId = 1L;
    Item existingItem = new Item();
    existingItem.setId(itemId);
    existingItem.setName("Existing Item");
    existingItem.setDescription("This is an existing item");

    when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));

    // Act
    boolean result = itemService.deleteItem(itemId);

    // Assert
    assertTrue(result);
    verify(itemRepository, times(1)).findById(itemId);
    verify(itemRepository, times(1)).delete(existingItem);
  }

  @Test
  void deleteItem_WithInvalidId_ReturnsFalse() {
    // Arrange
    Long itemId = 1L;

    when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

    // Act
    boolean result = itemService.deleteItem(itemId);

    // Assert
    assertFalse(result);
    verify(itemRepository, times(1)).findById(itemId);
    verify(itemRepository, never()).delete(any(Item.class));
  }

  @Test
  void getItemById_WithValidId_ReturnsItem() {
    // Arrange
    Long itemId = 1L;
    Item existingItem = new Item();
    existingItem.setId(itemId);
    existingItem.setName("Existing Item");
    existingItem.setDescription("This is an existing item");

    when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));

    // Act
    Item result = itemService.getItemById(itemId);

    // Assert
    assertNotNull(result);
    assertEquals(itemId, result.getId());
    assertEquals("Existing Item", result.getName());
    assertEquals("This is an existing item", result.getDescription());
    verify(itemRepository, times(1)).findById(itemId);
  }

  @Test
  void getItemById_WithInvalidId_ReturnsNull() {
    // Arrange
    Long itemId = 1L;

    when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

    // Act
    Item result = itemService.getItemById(itemId);

    // Assert
    assertNull(result);
    verify(itemRepository, times(1)).findById(itemId);
  }

  @Test
  void getAllItems_ReturnsAllItems() {
    // Arrange
    List<Item> itemList = new ArrayList<>();
    Item item1 = new Item();
    item1.setId(1L);
    item1.setName("Item 1");
    item1.setDescription("This is item 1");
    itemList.add(item1);
    Item item2 = new Item();
    item2.setId(2L);
    item2.setName("Item 2");
    item2.setDescription("This is item 2");
    itemList.add(item2);

    when(itemRepository.findAll()).thenReturn(itemList);

    // Act
    List<Item> result = itemService.getAllItems();

    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("Item 1", result.get(0).getName());
    assertEquals("This is item 1", result.get(0).getDescription());
    assertEquals("Item 2", result.get(1).getName());
    assertEquals("This is item 2", result.get(1).getDescription());
    verify(itemRepository, times(1)).findAll();
  }
}
