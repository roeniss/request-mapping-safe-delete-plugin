# Request Mapping Safe Delete

This is the IntelliJ plugin for deleting class-level `@RequestMapping` annotations without losing the path value.

<img width="600" alt="thumbnail" src="https://github.com/user-attachments/assets/98cea9f5-27a8-4454-b592-a98dbffbea33">

https://plugins.jetbrains.com/plugin/25247-request-mapping-safe-delete/

## Motivation

I think the mixed use of class-level `@RequestMapping` and method-level `@RequestMapping` is not a good approach.

It's good for reusability, but bad for code searching. So I prefer to use only method-level ones.

## How it works

I had to get familiar with Kotlin PSI. It's something like AST (at this point you'd probably know that I've failed to be friends with it).

When you execute the plugin (the command name is '`Safe Delete @RequestMapping`'), every files are scanned. For each file, if it's a Kotlin file with RequestMapping annotations, the executor starts operating.

Check out test codes so that you know what could happen.

## Caveat

Not all formats are properly treated. I tried to cover all the cases, but I succeeded with only the basic form.

If your cases are not covered, please let me know.
