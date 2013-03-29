# jmx-search module

This module is what connects to the remote MBean server.

## Building

Run `mvn clean compile assembly:single`. This produces a JAR with all dependencies bundled within. 

## Executing

First you'll need to register the jmx-search mbean. To do so, simply run:

    java -jar target/jmx-search-1.0-SNAPSHOT-jar-with-dependencies.jar -h localhost -t 8686 -u admin -r

Then you can invoke the bean; for example:

	java -jar target/jmx-search-1.0-SNAPSHOT-jar-with-dependencies.jar -h localhost -t 8686 -u admin -q search-term

Remember to replace the username (the `-u` flag) and add the `-p` flag if a password is required.

If you don't want the mbean on the server any longer, just use the `-d` flag.
