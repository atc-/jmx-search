JMX Classpath Searching MBean
=============================
This tool provides a non-production-use utility for listing the contents of a Sun Java HotSpot VM classloader running 
in a JEE container. 

Please note: this is NOT for production-system use.


Installation Steps
==================
1. Execute `mvn install`

2. Deploy the classpath-mbean/target/*.jar to your container

3. Execute `java -jar -h yourhostname -t 8686 -u admin -p 1234 -r` to register the jmx-search mbean

4. Connect jconsole to your container and browse the org.atc MBean contents

