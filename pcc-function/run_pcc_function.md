# Run PCC function

The function need to be deployed to PCC cluster before you can run it. You can use the `gfsh` command line to run the function or you can write an application to run the function.

## Running the Function Using gfsh

1. Start a `gfsh` prompt.
2. If necessary, start a locator and connect to the cluster where you want to run the function.
3. At the `gfsh` prompt, type the following command:

    ```
    gfsh> execute function --id=function_id
    ```
Where function_id equals the unique ID assigned to the function. You can obtain this ID using the `Function.getId` method.

See Function Execution Commands for more `gfsh` commands related to functions.

### Running the Function via Java API Calls

1. Use one of the `FunctionService` `on*` methods to create an `Execution` object. The `on*` methods, `onRegion`, `onMembers`, etc., define the highest level where the function is run. For colocated partitioned regions, use `onRegio`n and specify any one of the colocated regions. The function run using `onRegion` is referred to as a data dependent function - the others as data-independent functions.
2. Use the `Execution` object as needed for additional function configuration. You can:
    - Provide a key Set to `withFilters` to narrow the execution scope for `onRegion` Execution objects. You can retrieve the key set in your Function execute method through `RegionFunctionContext.getFilter`.
    - Provide function arguments to `setArguments`. You can retrieve these in your Function execute method through `FunctionContext.getArguments`.
    - Define a custom `ResultCollector`
3. Call the `Execution` object to execute method to run the function.
4. If the function returns results, call `getResult` from the results collector returned from execute and code your application to do whatever it needs to do with the results. Note: For high availability, you must call the `getResult` method.

### Running the Function using Spring Data GemFire framework
