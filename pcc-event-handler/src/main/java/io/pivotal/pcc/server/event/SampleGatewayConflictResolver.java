package io.pivotal.pcc.server.event;

import org.apache.geode.cache.Operation;
import org.apache.geode.cache.util.GatewayConflictHelper;
import org.apache.geode.cache.util.GatewayConflictResolver;
import org.apache.geode.cache.util.TimestampedEntryEvent;
import org.apache.geode.pdx.PdxInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Heng Yan
 */
public class SampleGatewayConflictResolver implements GatewayConflictResolver {

    final static Logger logger = LogManager.getLogger(SampleGatewayConflictResolver.class);

    @Override
    public void onEvent(final TimestampedEntryEvent te, final GatewayConflictHelper conflictHelper) {

        if (te.getOperation().isUpdate()) {
            int newDisId = te.getNewDistributedSystemID();
            long newTimestamp = te.getNewTimestamp();
            int oldDisId = te.getOldDistributedSystemID();
            long oldTimestamp = te.getOldTimestamp();

            logger.info("The received update is from cluster:{} at {}. The current entry in the cache was last updated by cluster:{} at {}.", newDisId, newTimestamp, oldDisId, oldTimestamp);

            final PdxInstance newValue = (PdxInstance)te.getNewValue();
            final PdxInstance oldValue = (PdxInstance)te.getOldValue();

            /**
             * Extract one or multiple field(s) from local and remote cache entry and use them to build the logic.
             */
            final int newAreaCode = (Integer) newValue.getField("areaCode");
            final int oldAreaCode = (Integer) oldValue.getField("areaCode");

            /**
             * You need to decide how to resolve the conflict according to your business logic.
             *
             * In this example, to demonstrate the logic, let's assume there are only three possible area code (1: US, 2: EMEA: 3: Asia). US has the highest priority, EMEA has the second, and Asia has the last.
             *
             */

            if (newAreaCode == oldAreaCode) {

                // When both local and remote have the same areaCode, compare their timestamp to decide who wins.
                if (newTimestamp >= oldTimestamp) {

                    // Replace the local value with the new value.
                    conflictHelper.changeEventValue(newValue);

                } else {

                    // Ignore the new value.
                    conflictHelper.disallowEvent();

                }
            } else if (newAreaCode < oldAreaCode) {

                // Based on our assumption, US always win over EMEA/Aisa and EMEA always win over Aisa. Replace the local value with the new value.
                conflictHelper.changeEventValue(newValue);

            } else if (newAreaCode > oldAreaCode) {

                // Ignore the new value.
                conflictHelper.disallowEvent();

            }
        }

    }
}
