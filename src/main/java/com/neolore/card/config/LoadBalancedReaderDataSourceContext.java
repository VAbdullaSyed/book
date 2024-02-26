package com.neolore.card.config;

import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalancedReaderDataSourceContext {

  private static final ThreadLocal<AtomicInteger> READER_DATASOURCE_LEVEL =
      ThreadLocal.withInitial(() -> new AtomicInteger(0));

  private LoadBalancedReaderDataSourceContext() {
  }

  public static boolean isLoadBalancedReaderZone() {
    return READER_DATASOURCE_LEVEL.get().get() > 0;
  }

  public static void enter() {
    READER_DATASOURCE_LEVEL.get().incrementAndGet();
  }

  public static void exit() {
    READER_DATASOURCE_LEVEL.get().decrementAndGet();
  }
}

