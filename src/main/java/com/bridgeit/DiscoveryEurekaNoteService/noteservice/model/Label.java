 package com.bridgeit.DiscoveryEurekaNoteService.noteservice.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author LAKSHMI PRIYA
 * @since DATE:10-07-2018
 *        <p>
 *        <b>A POJO class for label class.</b>
 *        </p>
 */
@Document(indexName = "test1", type = "label")
public class Label {
	 private static final Logger logger = LoggerFactory.getLogger(Label.class);
	@Id
	private String id;
	private String labelName;
	private String note;
	private String user;
	
public Label() {
	 logger.debug("Log message at DEBUG level from Label constructor");
	 logger.info("Log message at INFO level from  Label constructor");
}

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}


	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

}
