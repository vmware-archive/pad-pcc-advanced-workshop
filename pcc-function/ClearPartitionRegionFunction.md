#ClearPartitionRegionFunction
You will get `java.lang.UnsupportedOperationException` when you use either `region.clear()` API or `gfsh>remove --region=orderRegion --all` command to remove all entries from a **PARTITION** region. This function helps you to remove all entries from a PARTITION region.
- Invoke the function with `onRegion` and not provide any filter. In this case, the function will be executed on all servers which host the partition data.
- Have the function return true from `optimizeForWrite`, so that it is executed only on primary buckets.
- On each server, using `PartitionRegionHelper.getLocalDataForContext(rfc)` to get the access to local primary data and remove them.
- Optionally, data will be removed in batches on each server to provide the optimal performance. `region.removeAll(keys)` will perform poorly if the size of keys is very big. It could even cause the server to be kicked out of the cluster.

### Step 1: Build the JAR file
~~~
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ pcc-functions ---
[INFO] Building jar: /Users/hyan/Documents/workspace/pad-pcc-advanced-workshop/pcc-function/target/pcc-functions-1.0-SNAPSHOT.jar
[INFO]
[INFO] --- maven-install-plugin:2.4:install (default-install) @ pcc-functions ---
[INFO] Installing /Users/hyan/Documents/workspace/pad-pcc-advanced-workshop/pcc-function/target/pcc-functions-1.0-SNAPSHOT.jar to /Users/hyan/.m2/repository/io/pivotal/pcc/pcc-functions/1.0-SNAPSHOT/pcc-functions-1.0-SNAPSHOT.jar
[INFO] Installing /Users/hyan/Documents/workspace/pad-pcc-advanced-workshop/pcc-function/pom.xml to /Users/hyan/.m2/repository/io/pivotal/pcc/pcc-functions/1.0-SNAPSHOT/pcc-functions-1.0-SNAPSHOT.pom
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 2.492 s
[INFO] Finished at: 2019-03-21T13:32:38-04:00
[INFO] Final Memory: 24M/312M
[INFO] ------------------------------------------------------------------------

Process finished with exit code 0
~~~

### Step 2: Deploy the JAR file to PCC cluster
```
gfsh>deploy --jar=pcc-functions-1.0-SNAPSHOT.jar
```

### Step 3: Execute the function from your application
~~~
Region<String, String>  orderRegion = ClientCacheFactory.getAnyInstance().getRegion("orderRegion");
ResultCollector rc = FunctionService.onRegion(orderRegion).execute("ClearPartitionRegionFunction");
List<String> results = (List<String>) rc.getResult();
~~~