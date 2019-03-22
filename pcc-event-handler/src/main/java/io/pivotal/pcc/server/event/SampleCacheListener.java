package io.pivotal.pcc.server.event;

import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.util.CacheListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Heng Yan
 */
public class SampleCacheListener extends CacheListenerAdapter<String, String> {

    final static Logger logger = LogManager.getLogger(SampleCacheListener.class);

    @Override
    public void afterCreate(final EntryEvent<String, String> event) {

        final String key = event.getKey();
        final String value = event.getNewValue();

        logger.info("Received afterCreate event for entry: key={}, value={}", key, value);

        // TODO: Implement the logic to handle the event according to your requirement.

        super.afterCreate(event);
    }

    @Override
    public void afterUpdate(final EntryEvent<String, String> event) {

        final String key = event.getKey();
        final String oldValue = event.getOldValue();
        final String newValue = event.getNewValue();

        logger.info("Received afterUpdate event for entry: key={}, oldValue={}, and newValue={}", key, oldValue, newValue);

        // TODO: Implement the logic to handle the event according to your requirement.

        super.afterUpdate(event);
    }

    @Override
    public void afterDestroy(final EntryEvent<String, String> event) {

        final String key = event.getKey();
        final String value = event.getOldValue();

        logger.info("Received afterDestroy event for entry: key={}, value={}", key, value);

        // TODO: Implement the logic to handle the event according to your requirement.

        super.afterDestroy(event);
    }
}
