#SmartQueryFunction
PCC has the ability to target specific node(s) in a query. If you know that a specific bucket(s) contains the data that you want to query, you can use a function to ensure that your query only runs the specific node(s) that holds the data. This can greatly improve query efficiency. This is done by executing the query in a function.

- Invoke the function with `onRegion` and provide customerId as the filter in `withFilter` (assume order data is partitioned by customerId).
- Have the function return true from `optimizeForWrite`, so that it is executed only on primary buckets.
- Use the Query Execute API with a `RegionFunctionContext` in the function. Otherwise, you could end up executing the same query on more than one member. 

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
String customerId = "heng_yan";
Region<String, String>  orderRegion = ClientCacheFactory.getAnyInstance().getRegion("orderRegion");
String queryStr = "SELECT * FROM /orderRegion WHERE customerId = ?";
ResultCollector rc = FunctionService.onRegion(orderRegion).setArguments(queryStr).withFilter(Collections.singleton(customerId)).execute("SmartQueryFunction");
List<String> results = (List<String>) rc.getResult();
~~~