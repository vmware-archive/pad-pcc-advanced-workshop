package io.pivotal.pcc.server.function;


import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.execute.*;
import org.apache.geode.cache.partition.PartitionRegionHelper;
import org.apache.geode.cache.query.Query;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.SelectResults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;

/**
 * @author Heng Yan
 */
public class SmartQueryFunction implements Function {

    final static Logger logger = LogManager.getLogger(SmartQueryFunction.class);

    @Override
    public void execute(final FunctionContext fc) {

        if (!(fc instanceof RegionFunctionContext)) {
            throw new FunctionException("This is a data aware function, and has to be executed on region using FunctionService.onRegion.");
        }

        final RegionFunctionContext rfc = (RegionFunctionContext) fc;
        if (!PartitionRegionHelper.isPartitionedRegion(rfc.getDataSet())) {
            throw new FunctionException("This function is to demonstrate data aware capability, and has to be executed on a Partition region.");
        }

        final String queryStr = (String) rfc.getArguments();
        final ResultSender<List> resultSender = rfc.getResultSender();
        final String customerId = (String) rfc.getFilter().iterator().next();

        try {
            final Cache cache = CacheFactory.getAnyInstance();
            final QueryService queryService = cache.getQueryService();
            final Query query = queryService.newQuery(queryStr);

            /**
             * Executes the query on the partitioned region associated with the given RegionFunctionContext which targets the query on subset of data.
             */
            final SelectResults results = (SelectResults) query.execute(rfc, customerId);

            resultSender.sendResult(results.asList());
            resultSender.lastResult(null);

        } catch (final Exception e) {
            throw new FunctionException(e);
        }
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

    @Override
    public String getId() {
        return getClass().getName();
    }
}
