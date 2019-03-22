# Deploy PCC function

When you deploy a JAR file that contains a Function (in other words, contains a class that implements the `Function` interface), the Function will be automatically registered via the `FunctionService.registerFunction` method.

To register a function by using `gfsh`:

1. Package your class files into a JAR file.
2. Start a `gfsh` prompt. If necessary, start a locator and connect to the cluster where you want to run the function.
3. At the `gfsh` prompt, type the following command:
```
gfsh>deploy --jar=pcc-functions-1.0-SNAPSHOT.jar
```
where group1_functions.jar corresponds to the JAR file that you created in step 1.

If another JAR file is deployed (either with the same JAR filename or another filename) with the same Function, the new implementation of the Function will be registered, overwriting the old one. If a JAR file is undeployed, any Functions that were auto-registered at the time of deployment will be unregistered. Since deploying a JAR file that has the same name multiple times results in the JAR being un-deployed and re-deployed, Functions in the JAR will be unregistered and re-registered each time this occurs. If a Function with the same ID is registered from multiple differently named JAR files, the Function will be unregistered if either of those JAR files is re-deployed or un-deployed.