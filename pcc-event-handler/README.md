# PCC Event Handler

PCC provides many types of events and event handlers to help you manage your different data and application needs.

### CacheLoader
Allows data from outside of the VM to be placed into a region. When `Region.get(Object)` is called for a region entry that has a null value, the load method of the region's cache loader is invoked. The load method creates the value for the desired key by performing an operation such as a database query. The load may also perform a net search that will look for the value in a cache instance hosted by another member of the distributed system. The typical use case of `CacheLoader` is to do lazy-load on cache miss.

#### Step 1: Build the JAR file
#### Step 2: Deploy the JAR file to PCC cluster
```
gfsh>deploy --jar=pcc-event-handler-1.0-SNAPSHOT.jar
```
#### Step 3: Create Region and attach CacheLoader to it
~~~
gfsh>create region --name=orderRegion --type=PARTITION_REDUNDANT --cache-loader=io.pivotal.pcc.server.event.SampleCacheLoader
~~~

#### [Sample Code](src/main/java/io/pivotal/pcc/server/event/SampleCacheLoader.java)

##

### CacheWriter
A user-defined object installed in region that is called **synchronously BEFORE a region or entry in the cache is modified**. The typical use for a `CacheWriter` is to update a database (**Write-through Pattern**). Application writers should implement these methods to execute application-specific behavior before the cache is modified.
Before the region is updated via a `put`, `create`, or `destroy` operation, PCC will call a `CacheWriter` that is installed anywhere in any participating cache for that region, preferring a local `CacheWriter` if there is one. Usually there will be only one `CacheWriter` in the distributed system. If there are multiple CacheWriters available in the distributed system, the GemFire implementation always prefers one that is stored locally, or else picks one arbitrarily; in any case only one `CacheWriter` will be invoked.

The `CacheWriter` is capable of aborting the update to the cache by throwing a `CacheWriterException`. This exception or any runtime exception thrown by the `CacheWriter` will abort the operation and the exception will be propagated to the initiator of the operation, regardless of whether the initiator is in the same VM as the `CacheWriter`.

#### Step 1: Build the JAR file
#### Step 2: Deploy the JAR file to PCC cluster
```
gfsh>deploy --jar=pcc-event-handler-1.0-SNAPSHOT.jar
```
#### Step 3: Create Region and attach CacheWriter to it
~~~
gfsh>create region --name=orderRegion --type=PARTITION_REDUNDANT --cache-writer=io.pivotal.pcc.server.event.SampleCacheWriter
~~~

#### [Sample Code](src/main/java/io/pivotal/pcc/server/event/SampleCacheWriter.java)

##

### CacheListener
A user-defined object installed in region that is called **synchronously AFTER a region or entry in the cache is modified**. Receives only local cache events. After the region is updated via a `put`, `create`, or `destroy` operation, PCC will call a `CacheListener` that is installed in the region. The update call will not return to the caller until the `CacheListener` finishes the processing. Install one in every member where you want the events handled by this listener. In a partitioned region, the cache listener only fires in the primary data store. Listeners on secondaries are not fired.

#### Step 1: Build the JAR file
#### Step 2: Deploy the JAR file to PCC cluster
```
gfsh>deploy --jar=pcc-event-handler-1.0-SNAPSHOT.jar
```
#### Step 3: Create Region and attach CacheListener to it

~~~
gfsh>create region --name=orderRegion --type=PARTITION_REDUNDANT --cache-listener=io.pivotal.pcc.server.event.SampleCacheListener
~~~
#####

   You may attach multiple `CacheListener` to a region.
~~~
gfsh>create region --name=orderRegion --type=PARTITION_REDUNDANT --cache-listener=io.pivotal.pcc.server.event.MyCacheListener1,io.pivotal.pcc.server.event.MyCacheListener2
~~~

#### [Sample Code](src/main/java/io/pivotal/pcc/server/event/SampleCacheListener.java)

##

### AsyncEventListener
A user-defined object for events passing through the `AsyncEventQueue` to which this listener is attached. `AsyncEventQueue` is installed in region. After the region is updated via a `put`, `create`, or `destroy` operation, `AsyncEvents` are enqueued to `AsyncEventQueue` for later processing. Implementers of interface `AsyncEventListener` process batches of `AsyncEvent` delivered by the corresponding `AsyncEventQueue`. The frequency of asynchronous processing is determined by the configurable parameters, `batch-size` and `batch-time-interval`. The listener will start processing as soon as either of them is met.

The typical usecase:
   - Tracks changes in a region for write-behind processing (**Write-behind Pattern**).
   - Long running server side processing, for example, risk calculation, real-time analytics, data aggregation and etc.

#### Step 1: Build the JAR file
#### Step 2: Deploy the JAR file to PCC cluster
```
gfsh>deploy --jar=pcc-event-handler-1.0-SNAPSHOT.jar
```

#### Step 3: Create AsyncEventQueue
~~~
gfsh>create async-event-queue --id=myAEQ --parallel=true --batch-size=100 --batch-time-interval=5 --listener=io.pivotal.pcc.server.event.SampleAsyncEventListener --persistent=true --disk-store=myDiskStore -–max-queue-memory=128

~~~
#### Step 4: Create Region and attach AsyncEventQueue to it

~~~
gfsh>create region --name=orderRegion --type=PARTITION_REDUNDANT --async-event-queue-id=myAEQ
~~~

#### [Sample Code](src/main/java/io/pivotal/pcc/server/event/SampleAsyncEventListener.java)

##

### GatewayConflictResolver
`GatewayConflictResolver` is a Cache-level plugin that is called upon to decide what to do with events that originate in other systems and arrive through the WAN Gateway. A `GatewayConflictResolver` is invoked if the current value in a cache entry was established by a different distributed system (with a different `distributed-system-id`) than an event that is attempting to modify the entry. It is not invoked if the event has the same distributed system ID as the event that last changed the entry.

You will need to implement `GatewayConflicResolver` only when you want to override the default consistency checking behavior. By default, all regions perform consistency checks when a member applies an update received either from another cluster member or from a remote cluster over the WAN based on timestamp.

#### [Sample Code](src/main/java/io/pivotal/pcc/server/event/SampleGatewayConflictResolver.java)