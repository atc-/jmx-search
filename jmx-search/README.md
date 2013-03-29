# jmx-search module

This module is what connects to the remote MBean server.

## Building

Run `mvn clean compile assembly:single`. This produces a JAR with all dependencies bundled within. 

## Executing

To run jmx-search, do:

	java -jar target/jmx-search-1.0-SNAPSHOT-jar-with-dependencies.jar -h yourhostname -t 8686 -u admin -p 1234 -r -q my-search-term -m mbeanName
