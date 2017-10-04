### Step 1

Implement an async Service, UserDirectory, using the core Finagle API, which takes an Int and looks up a corresponding name.
If there is no name found, throw an UnknownId.

The known users are:
   - id 1 -> Rita
   - id 2 -> Sue
   - id 3 -> Bob
