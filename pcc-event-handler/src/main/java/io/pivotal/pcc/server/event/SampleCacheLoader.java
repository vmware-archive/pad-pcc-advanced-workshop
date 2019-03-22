package io.pivotal.pcc.server.event;

import org.apache.geode.cache.CacheLoader;
import org.apache.geode.cache.CacheLoaderException;
import org.apache.geode.cache.LoaderHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Heng Yan
 */
public class SampleCacheLoader implements CacheLoader<String, String> {

    final static Logger logger = LogManager.getLogger(SampleCacheLoader.class);

    @Override
    public String load(final LoaderHelper<String, String> loaderHelper) throws CacheLoaderException {

        final String key = loaderHelper.getKey();

        logger.info("CacheLoader is invoked to retrieve value for key: {}", key);

        return loadFromDatabase(key);

    }

    private String loadFromDatabase(final String key) {

        //TODO: Implement the logic to load missing value from backend database or any other external system.

        return "dumy_value";
    }
}
