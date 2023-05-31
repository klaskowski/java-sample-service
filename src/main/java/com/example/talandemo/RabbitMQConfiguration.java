package com.example.talandemo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

  private static final String EXCHANGE_NAME = "http-traces-exchange";
  private static final String QUEUE_NAME = "http-traces-queue";
  private static final String ROUTING_KEY = "http-traces";

  @Bean
  public Exchange topicExchange() {
    return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
  }

  @Bean
  public Queue topicQueue() {
    return QueueBuilder.durable(QUEUE_NAME).build();
  }

  @Bean
  public Binding topicBinding() {
    return BindingBuilder.bind(topicQueue()).to(topicExchange()).with(ROUTING_KEY).noargs();
  }
}
