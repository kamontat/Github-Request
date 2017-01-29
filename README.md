# Github-Request
request issue in github

# Command
- `./gradlew <command>` (in linux or macOS)
- `gradlew <command>` (in window)

## Build Setup
- `init` - Initializes a new Gradle build. (NO NEED)
- `wrapper` - Generates Gradle wrapper files. (NO NEED)

## Build
- `assemble` - Assembles the outputs of this project.
- `build` - Assembles and tests this project.
- `buildDependents` - Assembles and tests this project and all projects that depend on it.
- `buildNeeded` - Assembles and tests this project and all projects it depends on.
- `classes` - Assembles main classes.
- `clean` - Deletes the build directory.
- `jar` - Assembles a jar archive containing the main classes.
- `testClasses` - Assembles test classes.

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

### Unit-test

```
/build/reports/tests/test/index.html
```
or 
```
/build/reports/tests/index.html
```