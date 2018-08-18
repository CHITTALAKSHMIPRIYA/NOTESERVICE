package com.bridgeit.DiscoveryEurekaNoteService.noteservice.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bridgeit.DiscoveryEurekaNoteService.noteservice.model.Note;

/**
 * @author LAKSHMI PRIYA
 * @since DATE:10-07-2018
 *        <p>
 *        <b>Interface NoteDao extending MongoRepository.</b>
 *        </p>
 */
@Repository
public interface INoteDao extends MongoRepository<Note, String> {
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
