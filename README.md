##Requirements

java 8 and following maven dependencies
junit 5, hamcrest and maven-surefire-plugin

##Running the Tests

- Execute `git https://github.com/ashraf706/cs.git` to clone the public repository to your local machine.
- From command line change directory and go to the downloaded `cs` directory.
- To run the test execute `mvn test` from command line. 
- Please make sure you have the maven dependencies install in your local maven repository in case you have problem running the tests. 
- You can also execute `maven compile` to make sure all the dependencies are in place.

##Running the Application
- Executing `mvn install` should create  **log-parser.jar** with dependencies in the target folder.
- Run `java -jar log-parser.jar LOG_FILE_PATH`. Replace the `LOG_FILE_PATH` with a valid file path.
- For an example `java -jar log-parser.jar small-log.txt` produces the following output
  ![sample](./sample.png)

##Improvement I could make

- Didn't consider any bug in the log file or blank line etc. These things should be handled properly. 
- State should be **enumeration** rather than string.
- Provider could be implemented as a consumer as well.
- Log should be appended to a file.
- Didn't handle exception cleanly.