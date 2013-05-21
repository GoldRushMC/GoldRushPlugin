package com.goldrushmc.bukkit.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "job_tbl")
public class JobTbl {

	public enum EmployeeStatus {FULL_TIME, PART_TIME, CONTRACT}
	
	@Id @GeneratedValue private int id;
	@Column(name = "JOB_TITLE") private String jobTitle;
	@Enumerated private EmployeeStatus status;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}
	/**
	 * @param jobTitle the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	/**
	 * @return the status
	 */
	public EmployeeStatus getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(EmployeeStatus status) {
		this.status = status;
	}
}
