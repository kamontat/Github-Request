# Github-Request
request issue in github

# Content
- [Command](#command)
  - [Build Setup](#build-setup)
  - [Build](#build)
  - [Run](#run)
  - [Documentation](#documentation)
  - [Test](#test)
  - [Help](#help)
  - [More](#more)
- [Path](#path)
  - [JavaDoc](#javadoc)
  - [Unit Test](#unit-test)

# Command
- `./gradlew <command>` (in linux or macOS)
    - you can add `-q` to remove unnecessary output
- `gradlew <command>` (in window)
    - you can add `-q` to remove unnecessary output  

## Build Setup
- `init` - Initializes a new Gradle build. (NO NEED)
- `wrapper` - Generates Gradle wrapper files. (NO NEED)

## Build
- `assemble` - Assembles the outputs of this project.
- `build` - Assembles and tests this project.
- `distZip` - Bundles the project as a distribution.
- `buildDependents` - Assembles and tests this project and all projects that depend on it.
- `buildNeeded` - Assembles and tests this project and all projects it depends on.
- `classes` - Assembles main classes.
- `clean` - Deletes the build directory.
- `jar` - Assembles a jar archive containing the main classes.
- `testClasses` - Assembles test classes.

## Run
current you can't run gui unless you open the project by using `Intellij IDEA`
- `run` - Run default main which locate at `src/main/java/com/kamontat/main/Main.java`
- `execute` - run custom main class
  - you need to **path** main class correctly.
    - root directory is `src/main/java`.
    - you **DON'T** need to write any **extension** of the file 
      - example `Main` **NOT** `Main.java`
    - you have to use `.` instead of `/` 
      - example `com.kamontat.main.Main`
  - **normal run** 
    - `execute -P mainClass=<Name>` - to run main class by `<Name>`
      - example `execute -P mainClass=com.kamontat.main.Main`
  - **run with argument** 
    - `execute -P mainClass=<Name> -P arguments=<arguments>` - run main class by `<Name>` and input arguments separate each of argument by **,**
      - example `execute -P mainClass=com.kamontat.main.Main -P arguments=i,you,we,they`


## Documentation
- `javadoc` - Generates Javadoc API documentation for the main source code.

## Test
- `test` - Runs the unit tests.

## Help
- `buildEnvironment` - Displays all build script dependencies declared in root project.
- `components` - Displays the components produced by root project.
- `dependencies` - Displays all dependencies declared in root project.
- `dependencyInsight` - Displays the insight into a specific dependency in root project.
- `help` - Displays a help message.
- `model` - Displays the configuration model of root project.
- `projects` - Displays the sub-projects of root project.
- `properties` - Displays the properties of root project.

## More
- `tasks` - Displays the tasks runnable from root project.
- `tasks --all` - See all tasks and more detail
- `help --task <task>` - see more detail about a `<task>`

# Path
if you run command, here is path that you will get the result

### JavaDoc

``` 
/build/docs/javadoc/index/html 
```

### Unit Test

```
/build/reports/tests/test/index.html
```
or 
```
/build/reports/tests/index.html
```