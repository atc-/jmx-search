package org.atc.tools.jmx;

import static java.lang.String.format;
import gnu.getopt.Getopt;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang.StringUtils;
import org.atc.tools.jmx.domain.Options;

/**
 * The main class, responsible for command line option parsing and invoking the
 * JMX layer.
 * 
 * @author Alex Collins: github.com/atc- alexcollins.org
 */
public class JMXSearch {

	private static final String MBEAN_NAME = "org.atc:type=ClassloaderStats";
	private static final Logger log = Logger.getLogger("JMXSearch");

	/**
	 * Parse the arguments, validate, and execute the JMX layer if appropriate.
	 * 
	 * @param args
	 *            the command line arguments
	 * @throws IOException
	 *             if JMX connections couldn't be closed
	 */
	public static void main(final String[] args) throws IOException {
		final Options opts = parseCommandLineOptions(args);

		if (!valid(opts)) {
			// exit(4);
		}

		final String jmxURL = "service:jmx:rmi:///jndi/rmi://" + opts.getHostname() + ":" + opts.getPort()
				+ "/jmxrmi";
		log.info("Using JMXServiceURL string " + jmxURL);
		JMXConnector connector = null;

		try {
			connector = getConnector(jmxURL, opts);
			final MBeanServerConnection mbsc = connector.getMBeanServerConnection();
			log.info("Connection successful");

			// If still null == couldn't find mbean
			ObjectInstance objectInstance = null;
			try {
				objectInstance = mbsc.getObjectInstance(new ObjectName(MBEAN_NAME));
				log.info("Found instance " + objectInstance);
			} catch (final InstanceNotFoundException e) {
				log.info(format("Couldn't find object instance: %s", e));
			}

			if (opts.isRegister() && objectInstance == null) {
				log.info("Registering MBean...");
				registerMBean(mbsc, org.atc.tools.jmx.mbean.BasicClassloaderStats.class.getName(),
						new ObjectName(MBEAN_NAME));
				log.info("Registered");
			} else {
				log.info("MBean not registered as it already exists!");
			}

			if (opts.isUnregister()) {
				log.info("Unregistering MBean...");
				try {
					unregisterMBean(mbsc, new ObjectName(MBEAN_NAME));
					log.info("MBean removed");
				} catch (final InstanceNotFoundException unregisterEx) {
					log.info(format("Couldn't unregister MBean. Does it exist? %s", unregisterEx));
				}
			}
			
		} catch (final MalformedURLException e) {
			log.severe(format("Bad URL format! %s", e));
		} catch (final IOException e) {
			log.severe(format("I/O error! Reason: %s", e));
		} catch (final MalformedObjectNameException e) {
			log.severe(format("Couldn't find object instance: %s", e));
		} catch (final InstanceAlreadyExistsException e) {
			log.severe(format("Couldn't register MBean: %s", e));
		} catch (final MBeanRegistrationException e) {
			log.severe(format("Couldn't register MBean: %s", e));
			log.severe(String.valueOf(e.getCause()));
		} catch (final NotCompliantMBeanException e) {
			log.severe(format("Couldn't register MBean: %s", e));
		} catch (final ReflectionException e) {
			log.severe(format("Couldn't register MBean: %s", e));
		} catch (final MBeanException e) {
			log.severe(format("Couldn't register MBean: %s", e));
		} finally {
			if (connector != null) {
				connector.close();
			}
		}
	}

	/**
	 * Remove the MBean from the remote server
	 * 
	 * @param mbsc
	 *            our remote server connection
	 * @param objectName
	 *            the MBean to remove
	 * @throws InstanceNotFoundException
	 *             if the MBean couldn't be found
	 * @throws MBeanRegistrationException
	 *             if the bean couldn't be unregistered
	 * @throws IOException
	 *             should an I/O error occurr
	 */
	private static void unregisterMBean(final MBeanServerConnection mbsc, final ObjectName objectName)
			throws InstanceNotFoundException, MBeanRegistrationException, IOException {
		mbsc.unregisterMBean(objectName);
	}

	/**
	 * Get a JMXConnection for the given connector String
	 * 
	 * @param jmxGlassFishConnectorString
	 * @return
	 * @throws IOException
	 *             if connection to the remote server fails
	 */
	private static JMXConnector getConnector(final String jmxGlassFishConnectorString, final Options opts)
			throws IOException {
		final JMXServiceURL jmxUrl = new JMXServiceURL(jmxGlassFishConnectorString);
		final Map<String, Object> jmxEnv = new HashMap<String, Object>();
		final String[] credentials = new String[] { opts.getUsername(), opts.getPassword() };
		jmxEnv.put(JMXConnector.CREDENTIALS, credentials);

		log.info("Connecting to server...");
		JMXConnector connector = JMXConnectorFactory.connect(jmxUrl, jmxEnv);
		return connector;
	}

	private static void registerMBean(final MBeanServerConnection mbsc, final String beanName,
			final ObjectName objectName) throws ReflectionException, InstanceAlreadyExistsException,
			MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException,
			MalformedObjectNameException {
		mbsc.createMBean(beanName, objectName);
	}

	/**
	 * Returns true if all required parameters are set and valid, otherwise
	 * false.
	 * 
	 * @param opts
	 *            the {@code Options} instance to check
	 * @return true or false
	 */
	private static boolean valid(final Options opts) {
		return !StringUtils.isBlank(opts.getQuery());
	}

	/**
	 * Read the command line options in args and encapsulate them in an instance of Options.
	 * @param args the command line arguments
	 * @return an instance of Options
	 */
	private static Options parseCommandLineOptions(final String[] args) {
		final Getopt g = new Getopt("JMXSearch", args, "h:t:u:p:s:rd"); // TODO: document
		final Options opts = new Options();

		int c;
		while ((c = g.getopt()) != -1) {
			switch (c) {
			case 'h':
				opts.setHostname(g.getOptarg());
				break;
			case 't':
				opts.setPort(Integer.parseInt(g.getOptarg()));
				break;
			case 'u':
				opts.setUsername(g.getOptarg());
				break;
			case 'p':
				opts.setPassword(g.getOptarg());
				break;
			case 's':
				opts.setQuery(g.getOptarg());
				break;
			case 'r':
				opts.setRegister(true);
				break;
			case 'd':
				opts.setUnregister(true);
				break;
			default:
				log.severe(format("Unknown argument '%s'!", c));
				break;
			}
		}
		log.fine(format("Parsed arguments '%s'", opts));
		return opts;
	}
}
