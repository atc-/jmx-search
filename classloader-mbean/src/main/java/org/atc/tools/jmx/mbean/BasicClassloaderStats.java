package org.atc.tools.jmx.mbean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * Provides runtime statistics for classes loaded in a Sun JVM.
 * 
 * @author Alex Collins: github.com/atc- alexcollins.org
 */
public class BasicClassloaderStats implements ClassloaderStatsMBean {
	private static final Logger log = Logger.getLogger("BasicClassloaderStats");
	
	@Override
	public String[] getLoadedClasses() {
		final ClassLoader appLoader = ClassLoader.getSystemClassLoader();
		final ClassLoader currentLoader = this.getClass().getClassLoader();
		final ClassLoader[] loaders = new ClassLoader[] { appLoader, currentLoader };
		final Collection<String> classNames = new ArrayList<String>();
		
		for (final ClassLoader cl : loaders) {
			try {
				logFields(cl);
				
				final Field field = cl.getClass().getDeclaredField("classes");
				if (field != null) {
					field.setAccessible(true);
				}
				
				@SuppressWarnings("rawtypes")
				final Vector classes = (Vector) field.get(cl);
				for (final Object o : classes) {
					classNames.add(String.valueOf(o));
				}
			} catch (final NoSuchFieldException e) {
				log.severe("Field doesn't exist: " + e.getMessage());
			} catch (final SecurityException e) {
				log.severe("Security exception when finding classes: " + e.getMessage());
			} catch (final IllegalArgumentException e) {
				log.severe("Error collecting classes: " + e.getMessage());
			} catch (final IllegalAccessException e) {
				log.severe("Error collecting classes: " + e.getMessage());
			}
		}
		
		return classNames.toArray(new String[classNames.size()]);
	}

	private void logFields(final ClassLoader cl) {
		for (final Field f : cl.getClass().getFields()) {
			log.severe("Found field " + f.getName());
		}
	}
}
