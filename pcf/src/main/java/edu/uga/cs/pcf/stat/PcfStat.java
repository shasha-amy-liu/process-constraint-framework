package edu.uga.cs.pcf.stat;

import java.util.Date;

public class PcfStat {

	private String processName;
	private String serviceName;
	private String id;
	private long duration;
	private Date creationDate;

	public PcfStat(String processName, String serviceName, String id,
			long duration, Date creationDate) {
		super();
		this.processName = processName;
		this.serviceName = serviceName;
		this.id = id;
		this.duration = duration;
		this.creationDate = creationDate;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}
