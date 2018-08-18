
package com.bridgeit.DiscoveryEurekaNoteService.noteservice.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.bridgeit.DiscoveryEurekaNoteService.noteservice.model.Label;
import com.bridgeit.DiscoveryEurekaNoteService.noteservice.model.LabelDTO;



/**
 * @author LAKSHMI PRIYA
 * @since DATE:10-07-2018
 *        <p>
 *        <b>A POJO class with the user details.</b>
 *        </p>
 */

public interface ILabelElasticRepository extends ElasticsearchRepository<Label, String>{
	void deleteByLabelName(String labelName);

	/**
	 * @param label
	 */
	void save(LabelDTO label);
	public Label findByLabelName(String labelname); 
}
