package org.atc.tools.jmx.mbean;

import javax.management.MXBean;

/**
 * Defines methods for retrieving classloading stats from a JVM
 * @author Alex Collins: github.com/atc- alexcollins.org
 */
@MXBean
public interface ClassloaderStatsMBean {

	/**
	 * Return a String array of the classes loaded in the classloader.
	 * @return a String[], possibly empty, never null
	 */
	String[] getLoadedClasses();
}
