
package com.bridgeit.DiscoveryEurekaNoteService.noteservice.repository;

import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.bridgeit.DiscoveryEurekaNoteService.noteservice.model.Note;

/**
 * @author LAKSHMI PRIYA
 * @since DATE:10-07-2018
 *        <p>
 *        <b>A POJO class with the user details.</b>
 *        </p>
 */

public interface IElasticRepository extends ElasticsearchRepository<Note, String> {
	/**
	 * @param id
	 * @return
	 */
	Note findByNote(String id);

	/**
	 * @param string
	 * @return
	 */
	public List<Note> findByUser(String string);

}
