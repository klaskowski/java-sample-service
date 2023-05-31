package com.example.talandemo.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private ItemService itemService;

  @MockBean
  private RabbitTemplate rabbitTemplate;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void testCreateItem() throws Exception {
    Item item = new Item();
    item.setId(1L);
    item.setName("Sample Item");
    item.setPrice(10.99);
    item.setDescription("Sample description");

    when(itemService.createItem(any(Item.class))).thenReturn(item);

    mockMvc.perform(post("/items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                "{\"name\":\"Sample Item\",\"price\":10.99,\"description\":\"Sample description\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Sample Item"))
        .andExpect(jsonPath("$.price").value(10.99))
        .andExpect(jsonPath("$.description").value("Sample description"));
  }

  @Test
  void updateItem_ReturnsUpdatedItem() throws Exception {
    // Arrange
    Long itemId = 1L;
    Item updatedItem = new Item();
    updatedItem.setName("Updated Item");
    updatedItem.setDescription("This is the updated item");

    when(itemService.updateItem(eq(itemId), any(Item.class))).thenReturn(updatedItem);

    // Act & Assert
    mockMvc.perform(put("/items/{id}", itemId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Updated Item\",\"description\":\"This is the updated item\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Updated Item"))
        .andExpect(jsonPath("$.description").value("This is the updated item"));

    verify(itemService, times(1)).updateItem(eq(itemId), any(Item.class));
  }

  @Test
  void deleteItem_ReturnsNoContent() throws Exception {
    // Arrange
    Long itemId = 1L;

    when(itemService.deleteItem(itemId)).thenReturn(true);

    // Act & Assert
    mockMvc.perform(delete("/items/{id}", itemId))
        .andExpect(status().isNoContent());

    verify(itemService, times(1)).deleteItem(itemId);
  }

  @Test
  void getItemById_ReturnsItem() throws Exception {
    // Arrange
    Long itemId = 1L;
    Item item = new Item();
    item.setId(itemId);
    item.setName("Test Item");
    item.setDescription("This is a test item");

    when(itemService.getItemById(itemId)).thenReturn(item);

    // Act & Assert
    mockMvc.perform(get("/items/{id}", itemId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(itemId))
        .andExpect(jsonPath("$.name").value("Test Item"))
        .andExpect(jsonPath("$.description").value("This is a test item"));

    verify(itemService, times(1)).getItemById(itemId);
  }

  @Test
  void getAllItems_ReturnsAllItems() throws Exception {
    // Arrange
    List<Item> itemList = new ArrayList<>();
    itemList.add(new Item(1L, "Item 1", 10.0, "This is item 1"));
    itemList.add(new Item(2L, "Item 2", 11.0, "This is item 2"));

    Page<Item> itemPage = new PageImpl<>(itemList);

    when(itemService.getAllItems(any(Pageable.class))).thenReturn(itemPage);

    // Act & Assert
    mockMvc.perform(get("/items"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].id").value(1L))
        .andExpect(jsonPath("$.content[0].name").value("Item 1"))
        .andExpect(jsonPath("$.content[0].price").value(10.0))
        .andExpect(jsonPath("$.content[0].description").value("This is item 1"))
        .andExpect(jsonPath("$.content[1].id").value(2L))
        .andExpect(jsonPath("$.content[1].name").value("Item 2"))
        .andExpect(jsonPath("$.content[1].price").value(11.0))
        .andExpect(jsonPath("$.content[1].description").value("This is item 2"));

    verify(itemService, times(1)).getAllItems(any(Pageable.class));
  }
}