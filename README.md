JMX Classpath Searching MBean
=============================
This tool provides a non-production-use utility for listing the contents of a Sun Java HotSpot VM classloader running 
in a JEE container. 

Please note: this is NOT for production-system use.


Installation Steps
==================
1. Execute `mvn install`
2. Deploy the classpath-mbean/target/*.jar to your container
3. Execute `java -jar -h yourhostname -t 8686 -u admin -p 1234 -r -q my-search-term` -m mbeanName [searching for classnames isn't implemented yet]
4. [optional] Connect jconsole to your container and browse the org.atc MBean contents

