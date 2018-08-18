package com.bridgeit.DiscoveryEurekaNoteService.noteservice.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;

/**
 * @author LAKSHMI PRIYA
 * @since DATE:10-07-2018
 *        <p>
 *        <b>A POJO class with the user details.</b>
 *        </p>
 */

public class LabelDTO {
	 private static final Logger logger = LoggerFactory.getLogger(LabelDTO.class);
	@Id
	private String id;
	private String labelName;
	public LabelDTO() {
		 logger.debug("Log message at DEBUG level from Label constructor");
		 logger.info("Log message at INFO level from  label constructor");
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
}
