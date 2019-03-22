package io.pivotal.pcc.server.function;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.*;
import org.apache.geode.cache.partition.PartitionRegionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Heng Yan
 */
public class ClearPartitionRegionFunction implements Function {

    final static Logger logger = LogManager.getLogger(ClearPartitionRegionFunction.class);

    final private int batchSize = 5000;

    @Override
    public void execute(final FunctionContext fc) {

        if (!(fc instanceof RegionFunctionContext)) {
            throw new FunctionException("This is a data aware function, and has to be executed on region using FunctionService.onRegion.");
        }

        final RegionFunctionContext rfc = (RegionFunctionContext) fc;
        if (!PartitionRegionHelper.isPartitionedRegion(rfc.getDataSet())) {
            throw new FunctionException("This function is designed to clear all entries in a partition region, and has to be executed on a Partition region.");
        }

        final ResultSender<Boolean> resultSender = rfc.getResultSender();

        try {
            /**
             * Given a RegionFunctionContext for a partitioned Region, return a Region providing read access limited to the function context.
             * Returned Region provides only one copy of the data although redundantCopies configured is more than 0. If the invoking Function is configured to have optimizeForWrite as true,the returned Region will only contain primary copy of the data.
             */
            final Region<String, String> localPrimaryData = PartitionRegionHelper.getLocalDataForContext(rfc);
            int numLocalEntries = localPrimaryData.size();
            long end = 0;
            long start = System.currentTimeMillis();

            if (numLocalEntries <= batchSize) {
                localPrimaryData.removeAll(localPrimaryData.keySet());
            } else {
                final List buffer = new ArrayList(batchSize);
                int count = 0;
                for (final Object k : localPrimaryData.keySet()) {
                    buffer.add(k);
                    count++;
                    if (count == batchSize) {
                        localPrimaryData.removeAll(buffer);
                        buffer.clear();
                        count = 0;
                    } else {
                        continue;
                    }
                }
                localPrimaryData.removeAll(buffer);
            }
            end = System.currentTimeMillis();
            logger.info("{}: Cleared {} entries from {} region in {} ms.", Thread.currentThread().getName(), numLocalEntries, localPrimaryData.getName(), (end - start));
            resultSender.lastResult(Boolean.TRUE);
        } catch (final Exception e) {
            throw new FunctionException(e);
        }
    }
}
