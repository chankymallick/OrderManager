package com.ordermanager.utility;


import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class StartupHouseKeeper implements ApplicationListener<ContextRefreshedEvent> {

  @Override
  public void onApplicationEvent(final ContextRefreshedEvent event) {
      System.out.println("-----------------------ApplicationStartingUP");
  }
}