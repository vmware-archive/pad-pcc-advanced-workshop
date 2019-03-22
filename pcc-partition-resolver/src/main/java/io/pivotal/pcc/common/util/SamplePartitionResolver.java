package io.pivotal.pcc.common.util;

import org.apache.geode.cache.EntryOperation;
import org.apache.geode.cache.PartitionResolver;
import org.apache.geode.pdx.PdxInstance;

/**
 * @author Heng Yan
 */
public class SamplePartitionResolver implements PartitionResolver {

    @Override
    public Object getRoutingObject(final EntryOperation entryOperation) {
        final Object key = entryOperation.getKey();
        String customerId = null;
        if (key instanceof PdxInstance) {

            final PdxInstance k = (PdxInstance) key;
            customerId = (String)k.getField("customerId");

        }
// Only apply When you have domainClass.jar deployed in PCC cluster.
//        else if (key instanceof CustomerOrder) {
//              Order o = (CustomerOrder)key;
//              customerId = o.getCustomerId();
//        }
        if (customerId == null) {
            throw new IllegalArgumentException("The customerId must not be NULL!");
        }
        return customerId;
    }

    @Override
    public String getName() {
        return getClass().getName();
    }
}
