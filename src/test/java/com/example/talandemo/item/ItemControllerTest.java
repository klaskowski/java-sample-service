package com.example.talandemo.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    mockMvc.perform(MockMvcRequestBuilders.post("/items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                "{\"name\":\"Sample Item\",\"price\":10.99,\"description\":\"Sample description\"}"))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Sample Item"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(10.99))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Sample description"));
  }
}