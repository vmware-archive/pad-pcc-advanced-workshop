# Write the PCC Function Code
To write the function code, you implement the `Function` interface in the `org.apache.geode.cache.execute` package. Code the methods you need for the function. These steps do not have to be done in this order.
- Implement `getId` to return a unique name for your function. You can use this name to access the function through the `FunctionService` API.
- For high availability:
    1. Code `isHa` to return true to indicate to PCC that it can re-execute your function after one or more members fails
    2. Code your function to return a result
    3. Code `hasResult` to return true
- Code `hasResult` to return true if your function returns results to be processed and false if your function does not return any data - the fire and forget function.
- If the function will be executed on a region, implement `optimizeForWrite` to return false if your function only reads from the cache, and true if your function updates the cache. The method only works if, when you are running the function, the `Execution` object is obtained through a `FunctionService.onRegion` call. `optimizeForWrite` returns false by default. **To make the function data aware (for either read or write), `optimizeForWrite` must return true.**
- If the function should be run with an authorization level other than the default of DATA:WRITE, implement an override of the `Function.getRequiredPermissions()` method. See Authorization of Function Execution for details on this method.
- Code the `execute` method to perform the work of the function.
    - Make `execute` thread safe to accommodate simultaneous invocations.
    - For high availability, code execute to accommodate multiple identical calls to the function. Use the `RegionFunctionContext` `isPossibleDuplicate` to determine whether the call may be a high-availability re-execution. This boolean is set to true on execution failure and is false otherwise. Note: The isPossibleDuplicate boolean can be set following a failure from another member’s execution of the function, so it only indicates that the execution might be a repeat run in the current member.
    - Use the function context to get information about the execution and the data:
        - The context holds the function ID, the `ResultSender` object for passing results back to the originator, and function arguments provided by the member where the function originated.
        - The context provided to the function is the `FunctionContext`, which is automatically extended to `RegionFunctionContext` if you get the Execution object through a `FunctionService` `onRegion` call.
        - For data dependent functions, the `RegionFunctionContext` holds the `Region` object, the Set of key filters, and a boolean indicating multiple identical calls to the function, for high availability implementations.
        - For partitioned regions, the `PartitionRegionHelper` provides access to additional information and data for the region. For single regions, use `getLocalDataForContext`. For colocated regions, use `getLocalColocatedRegions`. Note: When you use `PartitionRegionHelper.getLocalDataForContext`, `putIfAbsent` may not return expected results if you are working on local data set instead of the region.
    - To propagate an error condition or exception back to the caller of the function, throw a `FunctionException` from the `execute` method. GemFire transmits the exception back to the caller as if it had been thrown on the calling side.