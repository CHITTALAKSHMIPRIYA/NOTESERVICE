
package com.bridgeit.DiscoveryEurekaNoteService.noteservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bridgeit.DiscoveryEurekaNoteService.noteservice.model.Label;
import com.bridgeit.DiscoveryEurekaNoteService.noteservice.model.LabelDTO;




/**
 * @author LAKSHMI PRIYA
 * @since DATE:10-07-2018
 *        <p>
 *        <b>A POJO class with the user details.</b>
 *        </p>
 */
@Repository
public interface ILabel extends MongoRepository<Label, String> {

	void deleteByLabelName(String labelName);

	/**
	 * @param label
	 */
	void save(LabelDTO label);
	public Label findByLabelName(String labelname); 

}
