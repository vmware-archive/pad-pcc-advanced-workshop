package io.pivotal.pcc.server.event;

import org.apache.geode.cache.CacheWriterException;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.util.CacheWriterAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Heng Yan
 */
public class SampleCacheWriter extends CacheWriterAdapter<String, String> {

    final static Logger logger = LogManager.getLogger(CacheWriterAdapter.class);

    @Override
    public void beforeCreate(final EntryEvent<String, String> event) throws CacheWriterException {
        final String key = event.getKey();
        final String value = event.getNewValue();

        logger.info("Received beforeCreate event for entry: key={}, value={}", key, value);

        // TODO: Implement the logic to handle the event according to your requirement.

        super.beforeCreate(event);
    }

    @Override
    public void beforeUpdate(final EntryEvent<String, String> event) throws CacheWriterException {
        final String key = event.getKey();
        final String oldValue = event.getOldValue();
        final String newValue = event.getNewValue();

        logger.info("Received beforeUpdate event for entry: key={}, oldValue={}, and newValue={}", key, oldValue, newValue);

        // TODO: Implement the logic to handle the event according to your requirement.

        super.beforeUpdate(event);

    }

    @Override
    public void beforeDestroy(final EntryEvent<String, String> event) throws CacheWriterException {

        final String key = event.getKey();
        final String value = event.getOldValue();

        logger.info("Received beforeDestroy event for entry: key={}, value={}", key, value);

        // TODO: Implement the logic to handle the event according to your requirement.

        super.beforeDestroy(event);

    }
}
