package com.bridgeit.DiscoveryEurekaNoteService.noteservice.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bridgeit.DiscoveryEurekaNoteService.noteservice.model.Label;
import com.bridgeit.DiscoveryEurekaNoteService.noteservice.model.LabelDTO;
import com.bridgeit.DiscoveryEurekaNoteService.noteservice.model.MetaData;
import com.bridgeit.DiscoveryEurekaNoteService.noteservice.model.Note;
import com.bridgeit.DiscoveryEurekaNoteService.noteservice.model.NoteDto;
import com.bridgeit.DiscoveryEurekaNoteService.noteservice.repository.IElasticRepository;
import com.bridgeit.DiscoveryEurekaNoteService.noteservice.repository.ILabel;
import com.bridgeit.DiscoveryEurekaNoteService.noteservice.repository.ILabelElasticRepository;
import com.bridgeit.DiscoveryEurekaNoteService.noteservice.repository.INoteDao;
import com.bridgeit.DiscoveryEurekaNoteService.utilservice.Messages;
import com.bridgeit.DiscoveryEurekaNoteService.utilservice.ModelMapperService;
import com.bridgeit.DiscoveryEurekaNoteService.utilservice.RestPreCondition;
import com.bridgeit.DiscoveryEurekaNoteService.utilservice.Utility;
import com.bridgeit.DiscoveryEurekaNoteService.utilservice.Exception.TodoException;

/**
 * @author LAKSHMI PRIYA
 * @since DATE:10-07-2018
 *        <p>
 *        <b>Note Service class implementing interface note service.</b>
 *        </p>
 */
@Service
public class NoteServiceImpl implements INoteService {

	@Autowired
	private INoteDao dao;
	@Autowired
	Utility util;
	@Autowired
	ModelMapperService model;
	@Autowired
	private ILabel ilabel;
	@Autowired
	Messages messages;
	@Autowired
	RestPreCondition restprecondition;
	@Autowired
	IElasticRepository elasticrepo;
	@Autowired
	ILabelElasticRepository labelelasticrepo;
	@Value("${patternString}")
	String patternString;

	public static final Logger LOG = LoggerFactory.getLogger(NoteServiceImpl.class);

	/******************************************************************************************************
	 * @param title
	 * @param description
	 * @param authorId
	 * @param id
	 *            <p>
	 *            <b> Method to create a new note</b>
	 *            </p>
	 * @throws TodoException
	 * @throws IOException
	 ******************************************************************************************************/
	public String createNote(NoteDto note, String userId) throws TodoException, IOException {
		MetaData data = new MetaData();
		restprecondition.checkNotNull(note.getDescription(), messages.get("100"));
		note.equals(note.getDescription());
		LOG.info(userId);
		if (note.getDescription().contains("https://") || note.getDescription().contains("http://")) {
			Document doc = Jsoup.connect(note.getDescription()).get();
			String title = doc.select("title").first().text();
			data.setTitle(title);
			String image = doc.select("img").first().attr("src");
			data.setImageUrl(image);
			note.getMetadataList().add(data);
		}
		/*
		 * Date now=new Date(); Instant current=now.toInstant(); LocalDateTime
		 * ldt=LocalDateTime.ofInstant(current,ZoneId.systemDefault());
		 */
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String date = formatter.format(new Date());
		Note note1 = model.mapper(note, Note.class);
		note1.setUser(userId);
		note1.setCreatedAt(date);
		note1.setUpdatedAt(date);
		dao.save(note1);
		elasticrepo.save(note1);
		return dao.save(note1).getNote();
	}

	/****************************************************************************************************
	 * @param noteId
	 * @param note
	 * @param token 
	 * @return
	 *         <p>
	 *         <b>Update a note</b>
	 *         </p>
	 * @throws TodoException
	 *****************************************************************************************************/
	@Override
	public String updateNote(String noteId, NoteDto note, String userId) throws TodoException {
		restprecondition.checkNotNull(note.getDescription(), Messages.get("100"));
		restprecondition.checkArgument(elasticrepo.existsById(noteId), Messages.get("101"));
		Optional<Note> note1 = elasticrepo.findById(noteId);
		Note note2 = model.mapper(note, Note.class);
		note2.setNote(noteId);
		note2.setUser(userId);
		note2.setCreatedAt(note1.get().getCreatedAt());
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		note2.setUpdatedAt(formatter.format(new Date()));
		dao.save(note2);
		elasticrepo.save(note2);
		return dao.save(note2).getUpdatedAt();

	}

	/*****************************************************************************************************
	 * @param noteId
	 * @param token
	 * @return
	 *         <p>
	 *         <b>Deleting a note</b>
	 *         </p>
	 * @throws TodoException
	 ****************************************************************************************************/
	@Override
	public void deleteNote(String noteId, String userId) throws TodoException {
		restprecondition.checkNotNull(noteId, Messages.get("102"));
		restprecondition.checkArgument(elasticrepo.existsById(noteId), Messages.get("101"));
		Note note = elasticrepo.findById(noteId).get();
		note.setTrashed(true);
		dao.deleteById(noteId);
		elasticrepo.deleteById(noteId);
	}

	/****************************************************************************************************
	 * @param noteId
	 * @param token
	 * @throws TodoException
	 *             <p>
	 *             <b>This method is used to get the data from trash to database</b>
	 *             </p>
	 *****************************************************************************************************/
	@Override
	public void restoreFromTrash(String noteId, String userId) throws TodoException {
		restprecondition.checkNotNull(noteId, Messages.get("102"));
		restprecondition.checkArgument(elasticrepo.existsById(noteId), Messages.get("101"));
		Note note = dao.findById(noteId).get();
		if (note.isTrashed()) {
			note.setTrashed(false);
			dao.save(note);
			elasticrepo.save(note);
		}
	}

	/******************************************************************************************************
	 * @param noteId
	 * @param token
	 * @throws TodoException
	 *             <p>
	 *             <b>Method to send the data from database to trash</b>
	 *             </p>
	 ******************************************************************************************************/
	@Override
	public void deleteTotrash(String noteId, String userId) throws TodoException {
		restprecondition.checkNotNull(noteId, Messages.get("102"));
		restprecondition.checkArgument(elasticrepo.existsById(noteId), Messages.get("101"));
		Note note = elasticrepo.findById(noteId).get();
		note.setTrashed(true);
		dao.save(note);
		elasticrepo.save(note);
	}

	/****************************************************************************************************
	 * @param noteId
	 * @param token
	 * @throws TodoException
	 *             <p>
	 *             <b>making a note as important</b>
	 *             </p>
	 ****************************************************************************************************/
	@Override
	public void pinNote(String noteId, String userId) throws TodoException {
		restprecondition.checkNotNull(noteId, Messages.get("102"));
		restprecondition.checkArgument(elasticrepo.existsById(noteId), Messages.get("101"));
		Note note = dao.findById(noteId).get();
		if (!note.isTrashed()) {
			note.setPin(true);
			dao.save(note);
			elasticrepo.save(note);
		}
	}

	/****************************************************************************************************
	 * @param noteId
	 * @param token
	 * @throws TodoException
	 *             <p>
	 *             <b>making a note unimportant</b>
	 *             </p>
	 ****************************************************************************************************/
	@Override
	public void unpinNote(String noteId, String userId) throws TodoException {
		restprecondition.checkNotNull(noteId, Messages.get("102"));
		restprecondition.checkArgument(elasticrepo.existsById(noteId), messages.get("101"));
		Note note = dao.findById(noteId).get();
		if (!note.isTrashed()) {
			note.setPin(false);
			dao.save(note);
			elasticrepo.save(note);
		}
	}

	/***************************************************************************************************
	 * @param noteId
	 * @param token
	 * @throws TodoException
	 ***************************************************************************************************/
	@Override
	public void archieve(String noteId, String userId) throws TodoException {
		restprecondition.checkNotNull(noteId, messages.get("102"));
		restprecondition.checkArgument(elasticrepo.existsById(noteId), messages.get("101"));
		Note note = elasticrepo.findById(noteId).get();
		if (!note.isTrashed()) {
			note.setArchieve(true);
			dao.save(note);
			elasticrepo.save(note);
		}
	}

	/*****************************************************************************************************
	 * @param token
	 * @param id
	 * @param reminderTime
	 * @return
	 * @throws Exception
	 *             <p>
	 *             <b>making a note to remind</b>
	 *             </p>
	 *****************************************************************************************************/
	@Override
	public Note setReminder(String token, String id, String reminderTime) throws TodoException, ParseException {

		Optional<Note> note = restprecondition.checkNotNull(elasticrepo.findById(id), messages.get("102"));
		Timer timer = null;
		if (note.isPresent()) {
			Date reminder = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(reminderTime);
			long timeDifference = reminder.getTime() - new Date().getTime();
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					System.out.println("Reminder task:" + note.toString());
				}
			}, timeDifference);
		}
		return note.get();
	}

	/***************************************************************************************************
	 * @param token
	 * @return
	 * @throws TodoException
	 *             <p>
	 *             <b>Display all the notes</b>
	 *             </p>
	 ****************************************************************************************************/
	@Override
	public List<Note> display(String userId) throws TodoException {
		List<Note> finalnoteList = new ArrayList<Note>();
		List<Note> notelist = elasticrepo.findByUser(userId);

		notelist.stream().filter(streamNote -> (streamNote.isPin() == true && streamNote.isTrashed == false
				&& streamNote.isArchieve() == false)).forEach(noteFilter -> finalnoteList.add(noteFilter));
		notelist.stream().filter(streamNote -> (streamNote.isPin() == false && streamNote.isTrashed == false
				&& streamNote.isArchieve() == false)).forEach(noteFilter -> finalnoteList.add(noteFilter));

		return finalnoteList;
	}

	/*****************************************************************************************************
	 * @param labelDto
	 * @param token
	 * @throws TodoException
	 *             <p>
	 *             <b>Creating a label</b>
	 *             </p>
	 *****************************************************************************************************/
	@Override
	public void createLabel(LabelDTO labelDto, String userId) throws TodoException {
		restprecondition.checkNotNull(labelDto.getLabelName(), messages.get("110"));
		Label label = model.mapper(labelDto, Label.class);
		label.setUser(userId);
		ilabel.save(label);
		labelelasticrepo.save(label);
	}

	/*************************************************************************************************
	 * @param labelName
	 * @param token
	 * @throws TodoException
	 *             <p>
	 *             <b>Deleting a label</b>
	 *             </p>
	 *************************************************************************************************/
	@Override
	public void deleteLabel(String labelName, String userId) throws TodoException {
		restprecondition.checkNotNull(labelName, messages.get("110"));
		ilabel.deleteByLabelName(labelName);
	}

	/**************************************************************************************************
	 * @param id
	 * @param label
	 * @param token
	 * @throws TodoException
	 *             <p>
	 *             <b>updating a label</b>
	 *             </p>
	 **************************************************************************************************/
	@Override
	public void updateLabel(String id, LabelDTO label, String token) throws TodoException {
		restprecondition.checkNotNull(label.getLabelName(), messages.get("110"));
		restprecondition.checkNotNull(token, messages.get("103"));
		restprecondition.checkArgument(ilabel.existsById(id), messages.get("111"));

		Optional<Label> label1 = ilabel.findById(id);
		Label label2 = model.mapper(label, Label.class);
		label2.setId(label1.get().getId());
		label2.setUser(label1.get().getUser());
		ilabel.save(label2);

	}

	/***************************************************************************************************
	 * @param token1
	 * @return
	 * @throws TodoException
	 *             <p>
	 *             <b>displaying a list of labels by sorting in alphabetical
	 *             order</b>
	 *             </p>
	 ***************************************************************************************************/
	@Override
	public List<Label> displayLabels(String userId,boolean descORasc) throws TodoException {
		List<Label> list = new ArrayList<>();
		list = ilabel.findAll();
		
		if(descORasc) {
	/*Collections.sort(list, (label1, label2) -> {
			return label1.getLabelName().compareTo(label2.getLabelName());
		});*/
			return list.stream().sorted(Comparator.comparing(Label::getLabelName)).collect(Collectors.toList());
}
		else		
			return list.stream().sorted(Comparator.comparing(Label::getLabelName).reversed()).collect(Collectors.toList());
		
	}

	/****************************************************************************************************
	 * @param note
	 * @param id
	 * @param token
	 * @throws TodoException
	 *             <p>
	 *             <b>Adding label to note</b>
	 *             </p>
	 ****************************************************************************************************/
	@Override
	public void addLabelToNote(String note, String labelName, String userId) throws TodoException {
		Optional<Note> optionalNote = elasticrepo.findById(note);
		List<Note> notelist = elasticrepo.findByUser(userId);
		LabelDTO label = new LabelDTO();
		restprecondition.checkArgument(elasticrepo.existsById(note), messages.get("101"));
		for (Note n : notelist) {
			if (n.getNote().equals(note)) {
				label.setLabelName(labelName);
				Label labelmap = model.mapper(label, Label.class);
				ilabel.save(labelmap);
				Note noteLabel = model.mapper(label, Note.class);
				n.getLabel().add(label);
				dao.save(n);
				elasticrepo.save(n);
			}
		}
	}

	/*************************************************************************************************
	 * @param labelName
	 * @param token
	 * @throws TodoException
	 *             <p>
	 *             <b>Method to remove label from note and label</b>
	 *             </p>
	 *************************************************************************************************/
	@Override
	public void removeLabel(String labelName, String userId) throws TodoException {
		restprecondition.checkNotNull(labelelasticrepo.findByLabelName(labelName), messages.get("112"));

		Optional<Label> labelFound = labelelasticrepo.findById(labelName);
		ilabel.deleteByLabelName(labelName);
		List<Note> notes = elasticrepo.findByUser(userId);

		for (int i = 0; i < notes.size(); i++) {

			for (int j = 0; j < notes.get(i).getLabel().size(); j++) {
				if (labelFound == null) {
					LOG.error(messages.get("111"));
					throw new TodoException(messages.get("111"));
				}

				if (labelName.equals(notes.get(i).getLabel().get(j).getLabelName())) {
					notes.get(i).getLabel().remove(j);
					Note note1 = notes.get(i);
					dao.save(note1);
					elasticrepo.save(note1);
					break;
				}
			}
		}
	}

	/***************************************************************************************************
	 * @param labelName
	 * @param token
	 * @throws TodoException
	 *             <p>
	 *             <b>Method to remove label from note </b>
	 *             </p>
	 ***************************************************************************************************/

	@Override
	public void removeLabelfromNote(String labelName, String userId) throws TodoException {
		restprecondition.checkNotNull(ilabel.findByLabelName(labelName), messages.get("112"));
		List<Note> notes = elasticrepo.findByUser(userId);

		for (int i = 0; i < notes.size(); i++) {
			for (int j = 0; j < notes.get(i).getLabel().size(); j++) {
				if (labelName.equals(notes.get(i).getLabel().get(j).getLabelName())) {
					notes.get(i).getLabel().remove(j);
					Note note1 = notes.get(i);
					dao.save(note1);
					elasticrepo.save(note1);
					break;
				}
			}
		}
	}

	/**************************************************************************************************
	 * @param labelId
	 * @param token
	 * @param newLabelName
	 * @throws ToDoException
	 *             <p>
	 *             <b>Method to rename a label in note as well as from label
	 *             table</b>
	 *             </p>
	 ***************************************************************************************************/
	@Override
	public void renameLabel(String labelname, String userId, String newLabelName) throws TodoException {
		restprecondition.checkNotNull(ilabel.findByLabelName(labelname), messages.get("112"));
		Label labelFound = ilabel.findByLabelName(labelname);
		labelFound.setLabelName(newLabelName);
		ilabel.save(labelFound);
		List<Note> notes = dao.findAll();
		for (int i = 0; i < notes.size(); i++) {
			for (int j = 0; j < notes.get(i).getLabel().size(); j++) {

				if (labelname.equals(notes.get(i).getLabel().get(j).getLabelName())) {
					notes.get(i).getLabel().get(j).setLabelName(newLabelName);
					Note note = notes.get(i);
					dao.save(note);
					break;
				}

			}
		}
	}

	/*****************************************************************************************************
	 * @param url
	 * @param userId
	 * @param noteId
	 * @return
	 * @throws IOException
	 *             <p>
	 *             <b>Method to add image to note</b>
	 *             </p>
	 *****************************************************************************************************/
	@Override
	public Optional<Note> addimageToNote(String URL, String userId, String noteId) throws IOException {
		MetaData metadata = new MetaData();
		Optional<Note> optionalNote = elasticrepo.findById(noteId);
		if (URL.contains("https://") || URL.contains("http://")) {
			String description = optionalNote.get().getDescription();
			optionalNote.get().setDescription(description);
			Document doc = Jsoup.connect(URL).get();
			String title = doc.select("title").first().text();
			metadata.setTitle(title);
			String image = doc.select("img").first().attr("src");
			metadata.setImageUrl(image);
			LOG.info("Meta Description:" + image);
			optionalNote.get().getMetadataList().add(metadata);
		}
		dao.save(optionalNote.get());
		elasticrepo.save(optionalNote.get());
		return optionalNote;

	}

	
}
