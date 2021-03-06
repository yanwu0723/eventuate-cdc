package io.eventuate.local.mysql.binlog;

import io.eventuate.local.common.BinlogEntryToPublishedEventConverter;
import io.eventuate.local.db.log.test.common.AbstractDbLogBasedCdcKafkaPublisherEventsTest;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MySqlBinlogCdcIntegrationTestConfiguration.class, KafkaOffsetStoreConfiguration.class})
public class MySQLCdcKafkaPublisherEventsTest extends AbstractDbLogBasedCdcKafkaPublisherEventsTest {
  @Autowired
  private MySqlBinaryLogClient mySqlBinaryLogClient;

  @Before
  @Override
  public void init() {
    super.init();

    mySqlBinaryLogClient.addBinlogEntryHandler(eventuateSchema,
            sourceTableNameSupplier.getSourceTableName(),
            new BinlogEntryToPublishedEventConverter(),
            cdcDataPublisher);

    testHelper.runInSeparateThread(mySqlBinaryLogClient::start);
  }

  @After
  @Override
  public void clear() {
    mySqlBinaryLogClient.stop();
  }
}
