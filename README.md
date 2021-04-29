JFXtras
=======

A supporting library for JavaFX, containing helper classes, extended layouts, controls and other interesting widgets.

## Project structure:

    Root project 'jfxtras-all'
    +--- Project ':agenda'
    +--- Project ':common'
    +--- Project ':controls'
    +--- Project ':fxml'
    +--- Project ':test-support'
    \--- Project ':window'
    
> **NOTE:** please use `./gradlew projects` to get an up-to-date list of projects


## How to use JFXtras

The easiest way to use JFXtras is by obtaining it via Maven.

### Maven Artifacts:

The `group-id` is `org.jfxtras`, the `artifact-id` depends on the project(s) that shall be used.

### Example

##### Using the Controls Project:

###### Maven:

    <dependency>
      <groupId>org.jfxtras</groupId>
      <artifactId>jfxtras-controls</artifactId>
      <version>8.0-r1-SNAPSHOT</version>
    </dependency>
    
###### Gradle:

    compile group: 'org.jfxtras', name: 'jfxtras-controls', version: '8.0-r1-SNAPSHOT'

## License

JFXtras uses the [new BSD](http://en.wikipedia.org/wiki/BSD_licenses#3-clause_license_.28.22Revised_BSD_License.22.2C_.22New_BSD_License.22.2C_or_.22Modified_BSD_License.22.29) license
