package com.example.talandemo.item;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
@AutoConfigureMockMvc
public class ItemIntegrationTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @AfterEach
  void tearDown() {
    itemRepository.deleteAll();
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  void insertAndListItems() throws Exception {
    // Arrange
    mockMvc.perform(post("/items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                "{\"name\":\"Item 1\",\"price\":10.99,\"description\":\"This is item 1\"}"))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(jsonPath("$.name").value("Item 1"))
        .andExpect(jsonPath("$.price").value(10.99))
        .andExpect(jsonPath("$.description").value("This is item 1"));

    mockMvc.perform(post("/items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                "{\"name\":\"Item 2\",\"price\":11.99,\"description\":\"This is item 2\"}"))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(jsonPath("$.name").value("Item 2"))
        .andExpect(jsonPath("$.price").value(11.99))
        .andExpect(jsonPath("$.description").value("This is item 2"));


    // Act & Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/items")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(jsonPath("$.content[0].name").value("Item 1"))
        .andExpect(jsonPath("$.content[0].price").value(10.99))
        .andExpect(jsonPath("$.content[0].description").value("This is item 1"))
        .andExpect(jsonPath("$.content[1].name").value("Item 2"))
        .andExpect(jsonPath("$.content[1].price").value(11.99))
        .andExpect(jsonPath("$.content[1].description").value("This is item 2"));
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  void updateItem() throws Exception {
    // Arrange
    var result = mockMvc.perform(post("/items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                "{\"name\":\"Item 1\",\"price\":10.99,\"description\":\"This is item 1\"}"))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Item 1"))
        .andExpect(jsonPath("$.price").value(10.99))
        .andExpect(jsonPath("$.description").value("This is item 1"))
        .andReturn()
        .getResponse()
        .getContentAsString();

    Item item = objectMapper.readValue(result, Item.class);

    mockMvc.perform(put("/items/" + item.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                "{\"name\":\"Item 1\",\"price\":11.99,\"description\":\"This is item 1\"}"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Item 1"))
        .andExpect(jsonPath("$.price").value(11.99))
        .andExpect(jsonPath("$.description").value("This is item 1"));


    // Act & Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/items")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].id").value(1))
        .andExpect(jsonPath("$.content[0].name").value("Item 1"))
        .andExpect(jsonPath("$.content[0].price").value(11.99))
        .andExpect(jsonPath("$.content[0].description").value("This is item 1"));
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  void deleteItem() throws Exception {
    // Arrange
    var result = mockMvc.perform(post("/items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                "{\"name\":\"Item 1\",\"price\":10.99,\"description\":\"This is item 1\"}"))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(jsonPath("$.name").value("Item 1"))
        .andExpect(jsonPath("$.price").value(10.99))
        .andExpect(jsonPath("$.description").value("This is item 1"))
        .andReturn()
        .getResponse()
        .getContentAsString();

    Item item = objectMapper.readValue(result, Item.class);

    mockMvc.perform(MockMvcRequestBuilders.delete("/items/" + item.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNoContent());


    // Act & Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/items")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(0));
  }

  @Test
  @WithMockUser(username = "user1", password = "pwd", roles = "USER")
  void paginateItems() throws Exception {
    // Arrange
    for(int i=1;i<=2;i++) {
      mockMvc.perform(post("/items")
          .contentType(MediaType.APPLICATION_JSON)
          .content(
              "{\"name\":\"Item " + i + "\",\"price\":10.99,\"description\":\"This is item " + i +
                  "\"}"));
    }

    // Act & Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/items?page=0&size=1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].name").value("Item 1"))
        .andExpect(jsonPath("$.content[0].price").value(10.99))
        .andExpect(jsonPath("$.content[0].description").value("This is item 1"));

    mockMvc.perform(MockMvcRequestBuilders.get("/items?page=1&size=1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].name").value("Item 2"))
        .andExpect(jsonPath("$.content[0].price").value(10.99))
        .andExpect(jsonPath("$.content[0].description").value("This is item 2"));
  }
}
