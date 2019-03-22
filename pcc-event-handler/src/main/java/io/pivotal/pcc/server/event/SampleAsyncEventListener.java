package io.pivotal.pcc.server.event;

import org.apache.geode.cache.Operation;
import org.apache.geode.cache.asyncqueue.AsyncEvent;
import org.apache.geode.cache.asyncqueue.AsyncEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @author Heng Yan
 */
public class SampleAsyncEventListener implements AsyncEventListener {

    final static Logger logger = LogManager.getLogger(SampleAsyncEventListener.class);

    @Override
    public boolean processEvents(final List<AsyncEvent> events) {

        logger.info("Start processing {} AsyncEvents ......", events.size());

        try {
            for (final AsyncEvent<String, String> e : events) {

                final Operation operation = e.getOperation();

                if (operation == Operation.UPDATE || operation == Operation.PUTALL_UPDATE) {

                    processUpdate(e);

                } else if (operation == Operation.CREATE || operation == Operation.PUTALL_UPDATE) {

                    processCreate(e);

                } else if (operation == Operation.REMOVE || operation == Operation.DESTROY || operation == Operation.REMOVEALL_DESTROY) {

                    processDelete(e);

                } else {
                    logger.warn("Unknown cache operation: {}", operation.toString());
                }
            }
        } catch (final Exception e) {
            logger.error("Caught Exception while processing AsyncEvents: ", e);
        }
        return true;
    }

    /**
     * Process an AsyncEvent generated by entry update in the region.
     *
     * @param e
     */
    private void processUpdate(final AsyncEvent<String, String> e) {

        final String key = e.getKey();
        final String value = e.getDeserializedValue();

        // TODO: Implement the logic to handle the event according to your requirement.

    }

    /**
     * Process an AsyncEvent generated by entry creation in the region.
     *
     * @param e
     */
    private void processCreate(final AsyncEvent<String, String> e) {

        final String key = e.getKey();
        final String value = e.getDeserializedValue();

        // TODO: Implement the logic to handle the event according to your requirement.
    }

    /**
     * Process an AsyncEvent generated by entry deletion in the region.
     *
     * @param e
     */
    private void processDelete(final AsyncEvent<String, String> e) {

        final String key = e.getKey();
        final String value = e.getDeserializedValue();

        // TODO: Implement the logic to handle the event according to your requirement.
    }
}