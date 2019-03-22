package io.pivotal.pcc.server.function;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.execute.FunctionService;
import org.apache.geode.cache.execute.ResultCollector;

import java.util.*;

public class SampleClient {

    public void dataAwareFunction() {
        String userName = "heng_yan";
        Region<String, String>  sampleRegion = ClientCacheFactory.getAnyInstance().getRegion("sampleRegion");
        Object[] args = new Object[1];
        args[0] = userName;
        Set<String> keys = new HashSet<>(Arrays.asList("key_1", "key_2", "key_3", "key_4", "key_5"));
        ResultCollector rc = FunctionService.onRegion(sampleRegion).setArguments(args).withFilter(keys).execute("SampleDataAwareFunction");
        List<String> results = (List<String>) rc.getResult();
    }

    public void smartQueryFunction() {
        String customerId = "heng_yan";
        Region<String, String>  orderRegion = ClientCacheFactory.getAnyInstance().getRegion("orderRegion");
        String queryStr = "SELECT * FROM /orderRegion WHERE customerId = ?";
        ResultCollector rc = FunctionService.onRegion(orderRegion).setArguments(queryStr).withFilter(Collections.singleton(customerId)).execute("SmartQueryFunction");
        List<String> results = (List<String>) rc.getResult();
    }

    public void clearPartitionRegionFunction() {
        Region<String, String>  orderRegion = ClientCacheFactory.getAnyInstance().getRegion("orderRegion");
        ResultCollector rc = FunctionService.onRegion(orderRegion).execute("ClearPartitionRegionFunction");
        List<String> results = (List<String>) rc.getResult();
    }
}
