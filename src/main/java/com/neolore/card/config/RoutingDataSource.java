package com.neolore.card.config;


import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

  private static final Logger LOGGER = LoggerFactory.getLogger(RoutingDataSource.class);

  private static final String WRITER = "writer";
  private static final String LOAD_BALANCED_READER = "load-balanced-reader";

  public RoutingDataSource(DataSource primary, DataSource secondary) {

    Map<Object, Object> dataSources = new HashMap<>();
    dataSources.put(WRITER, primary);
    dataSources.put(LOAD_BALANCED_READER, secondary);

    setTargetDataSources(dataSources);
  }

  @Override
  protected Object determineCurrentLookupKey() {
    String dataSourceMode = LoadBalancedReaderDataSourceContext.isLoadBalancedReaderZone() ? LOAD_BALANCED_READER : WRITER;

    // Testing data source switch
    LOGGER.info("Datasource: {}", dataSourceMode);

    return dataSourceMode;
  }

}
