package com.neolore.card.config;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class WithLoadBalancedDataSourceInterception implements Ordered {

  private static final Logger LOGGER = LoggerFactory.getLogger(WithLoadBalancedDataSourceInterception.class);

  @Around("@annotation(com.neolore.card.config.WithLoadBalancedReaderDataSource)")
  public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      LoadBalancedReaderDataSourceContext.enter();
      //Debug data source switch
      LOGGER.debug("Entering load-balanced-reader dataSource zone");
      return joinPoint.proceed();
    } finally {
      LOGGER.debug("Leaving load-balanced-reader dataSource zone");
      LoadBalancedReaderDataSourceContext.exit();
    }
  }

  @Override
  public int getOrder() {
    // We'd like this interceptor to be handled with a higher priority than @Transactional.
    // This guarantees that datasource zone is defined before a new transaction starts.
    return Ordered.LOWEST_PRECEDENCE - 1;
  }
}
