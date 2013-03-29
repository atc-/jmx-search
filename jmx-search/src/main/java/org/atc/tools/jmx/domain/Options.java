package org.atc.tools.jmx.domain;

public class Options {

	private String hostname;
	private int port;
	private String username;
	private String password;
	private String query;
	private boolean register = false;
	private boolean unregister = false;
	private String mbeanName;

	public Options() {
		super();
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public boolean isRegister() {
		return register;
	}

	public void setRegister(boolean register) {
		this.register = register;
	}

	public boolean isUnregister() {
		return unregister;
	}

	public void setUnregister(boolean unregister) {
		this.unregister = unregister;
	}

	public String getMbeanName() {
		return mbeanName;
	}

	public void setMbeanName(String mbeanName) {
		this.mbeanName = mbeanName;
	}

	@Override
	public String toString() {
		return "Options [hostname=" + hostname + ", port=" + port + ", username=" + username + ", password="
				+ password + ", query=" + query + ", register=" + register + ", unregister=" + unregister
				+ ", mbeanName=" + mbeanName + "]";
	}
}
