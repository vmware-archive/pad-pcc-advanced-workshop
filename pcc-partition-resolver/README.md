# PCC Partition Resolver

By default, GemFire partitions each data entry into a bucket using a hashing policy on the key. Additionally, the physical location of the key-value pair is abstracted away from the application. You can change these policies for a partitioned region by providing a standard custom partition resolver that maps entries in a custom manner.

To custom-partition your region data, follow two steps:

- Implement the interface `org.apache.geode.cache.PartitionResolver`
- Configure the regions

Implement the `PartitionResolver` interface in one of the following ways:
- Using a custom class. Implement the `PartitionResolver` within your class, and then specify your class as the partition resolver during region creation. (**Recommended**)
- Using the key’s class. Have the entry key’s class implement the PartitionResolver interface. (You will need to deploy your domainClass.jar to PCC cluster. )




### StringPrefixPartitionResolver
PCC provides a ready to use PartitionResolver that is an implementation of a string-based partition resolver in `org.apache.geode.cache.util.StringPrefixPartitionResolver`. This resolver does not require any further implementation. It groups entries into buckets based on a portion of the key. All keys must be strings, and each key must include a ’|’ character, which delimits the string. The substring that precedes the ’|’ delimiter within the key will be returned by `getRoutingObject`.

~~~
gfsh>create region --name=orderRegion --type=PARTITION_REDUNDANT --partition-resolver=org.apache.geode.cache.util.StringPrefixPartitionResolver
~~~
###

### Write your own PartitionResolver
Implement the PartitionResolver’s `getRoutingObject` method to return the routing object for each entry. A hash of that returned routing object determines the bucket. Therefore, `getRoutingObject` should return an object that, when run through its hashCode, directs grouped objects to the desired bucket.

**Note:** Only the key, as returned by `getKey`, should be used by `getRoutingObject`. Do not use the value associated with the key or any additional metadata in the implementation of `getRoutingObjec`t. Do not use `getOperation` or `getValue`.

#### Step 1: Build the JAR file
#### Step 2: Deploy the JAR file to PCC cluster
```
gfsh>deploy --jar=pcc-partition-resolver-1.0-SNAPSHOT.jar
```
#### Step 3: Create Region and attach CacheWriter to it
~~~
gfsh>create region --name=orderRegion --type=PARTITION_REDUNDANT --partition-resolver=io.pivotal.pcc.common.util.SamplePartitionResolver
~~~

###

### Source Code: [SamplePartitionResolver.java](src/main/java/io/pivotal/pcc/common/util/SamplePartitionResolver.java)