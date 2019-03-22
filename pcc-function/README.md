# PCC Function

A function is a body of code that resides on a server and that an application can invoke from a client or from another server without the need to send the function code itself. The caller can direct a data-dependent function to operate on a particular dataset, or can direct a data-independent function to operate on a particular server, member, or member group.

### Data-dependent Functions
The function need to be executed on a region. The method `FunctionService.onRegion()` directs a data-dependent function to execute on a specific region.
- On a **REPLICATE** region: The function will only be executed on one of all available servers since each server contains the complete data set. The locator decides which server the function will be executed on based on the load.
- On a **PARTITION** region: Optionally, you can specify a set of keys on which to run the function, `FunctionService.onRegion().withFilter()`.
    - Without filters: The function will be executed on all servers which host the partition data simultaneously.
    - With filters: The function will only be executed on server(s) which host the primary copy of partition data for given key(s) simultaneously.

### Data-independent Functions
- On a specific member or members—Execute the function within a peer-to-peer cluster, specifying the member or members where you want to run the function by using FunctionService methods `onMember()` and `onMembers()`.
- On a specific server or set of servers—If you are connected to a cluster as a client, you can execute the function on a server or servers configured for a specific connection pool, or on a server or servers connected to a given cache using the default connection pool. For data-independent functions on client/server architectures, a client invokes FunctionService methods `onServer()` or `onServers()`.

## Getting Started
- ### [Write PCC function code](https://github.com/Pivotal-Field-Engineering/pad-pcc-advanced-workshop/blob/master/pcc-function/write_pcc_function_code.md)
- ### [Deploy PCC function](https://github.com/Pivotal-Field-Engineering/pad-pcc-advanced-workshop/blob/master/pcc-function/deploy_pcc_function.md)
- ### [Run PCC function](https://github.com/Pivotal-Field-Engineering/pad-pcc-advanced-workshop/blob/master/pcc-function/run_pcc_function.md)

## Sample Functions
- ### [SampleDataAwareFunction](https://github.com/Pivotal-Field-Engineering/pad-pcc-advanced-workshop/blob/master/pcc-function/SampleDataAwareFunction.md)
- ### [SmartQueryFunction](https://github.com/Pivotal-Field-Engineering/pad-pcc-advanced-workshop/blob/master/pcc-function/SmartQueryFunction.md)
- ### [ClearPartitionRegionFunction](https://github.com/Pivotal-Field-Engineering/pad-pcc-advanced-workshop/blob/master/pcc-function/ClearPartitionRegionFunction.md)
