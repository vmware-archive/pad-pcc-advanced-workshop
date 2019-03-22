package io.pivotal.pcc.server.function;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.*;
import org.apache.geode.cache.partition.PartitionRegionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.Set;

/**
 * This function is to demonstrate how a data aware function should be implemented and results should be streamed back to the caller. The caller executes the function to retrieve multiple entries for a set of keys. All keys are passed in as filters so that the function will only be executed on the server(s) which hosts the primary copy of data. Notice: the function is for demonstration purpose only. region.getAll(keys) does the same trick.
 *
 * @author Heng Yan
 */
public class SampleDataAwareFunction implements Function {

    final static Logger logger = LogManager.getLogger(SampleDataAwareFunction.class);

    @Override
    public void execute(final FunctionContext fc) {

        if (!(fc instanceof RegionFunctionContext)) {
            throw new FunctionException("This is a data aware function, and has to be executed on region using FunctionService.onRegion.");
        }

        final RegionFunctionContext rfc = (RegionFunctionContext) fc;
        if (!PartitionRegionHelper.isPartitionedRegion(rfc.getDataSet())) {
            throw new FunctionException("This function is to demonstrate data aware capability, and has to be executed on a Partition region.");
        }

        /**
         * Client is able to pass in 0...n arguments to function. The arguments are not needed for this particular example.
         */
        final Object[] arguments = (Object[]) rfc.getArguments();
        if (arguments != null && arguments.length > 1) {
            String username = (String) arguments[0];
            logger.info("The caller of the function is '{}'.", username);
        }
        final ResultSender<String> resultSender = rfc.getResultSender();

        /**
         * Returns subset of keys (filter) provided by the invoking thread (aka routing objects). The set of filter keys are locally present in the datastore on the executing cluster member.
         */
        final Set<String> filters = (Set<String>) rfc.getFilter();


        /**
         * Given a RegionFunctionContext for a partitioned Region, return a Region providing read access limited to the function context.
         * Returned Region provides only one copy of the data although redundantCopies configured is more than 0. If the invoking Function is configured to have optimizeForWrite as true,the returned Region will only contain primary copy of the data.
         */
        final Region<String, String> localPrimaryData = PartitionRegionHelper.getLocalDataForContext(rfc);

        int size = filters.size();
        final Iterator<String> keys = filters.iterator();
        for (int i = 0; i < (size - 1); i++) {
            resultSender.sendResult(localPrimaryData.get(keys.next()));
        }

        /**
         * Send the very last result back to the caller and end the execution.
         */
        resultSender.lastResult(localPrimaryData.get(keys.next()));
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    @Override
    public boolean optimizeForWrite() {
        return true;
    }

    @Override
    public boolean hasResult() {
        return true;
    }

    @Override
    public boolean isHA() {
        return true;
    }
}
