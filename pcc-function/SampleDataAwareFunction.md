# SampleDataAwareFunction

This function is to demonstrate how a data aware function should be implemented and results should be streamed back to the caller. The caller executes the function to retrieve multiple entries for a set of keys. All keys are passed in as filters so that the function will only be executed on the server(s) which hosts the primary copy of data. Notice: the function is for demonstration purpose only. region.getAll(keys) does the same trick.

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

### Step 3: Execute the fucntion from your application
~~~
String userName = "heng_yan";
Region<String, String>  sampleRegion = ClientCacheFactory.getAnyInstance().getRegion("sampleRegion");
Object[] args = new Object[1];
args[0] = userName;
Set<String> keys = new HashSet<>(Arrays.asList("key_1", "key_2", "key_3", "key_4", "key_5"));
ResultCollector rc = FunctionService.onRegion(sampleRegion).setArguments(args).withFilter(keys).execute("SampleDataAwareFunction");
List<String> results = (List<String>) rc.getResult();
~~~