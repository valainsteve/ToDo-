package de.uniba.wiai.dsg.ajp.assignment2.literature.logic.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import de.uniba.wiai.dsg.ajp.assignment2.literature.logic.DatabaseService;
import de.uniba.wiai.dsg.ajp.assignment2.literature.logic.LiteratureDatabaseException;
import de.uniba.wiai.dsg.ajp.assignment2.literature.logic.ValidationHelper;
import de.uniba.wiai.dsg.ajp.assignment2.literature.logic.model.Author;
import de.uniba.wiai.dsg.ajp.assignment2.literature.logic.model.Database;
import de.uniba.wiai.dsg.ajp.assignment2.literature.logic.model.Publication;
import de.uniba.wiai.dsg.ajp.assignment2.literature.logic.model.PublicationType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class DatabaseServiceImpl implements DatabaseService {
	private Database database;

	DatabaseServiceImpl(Database database){
		this.database = database;
	}

	@Override
	public void addPublication(String title, int yearPublished, PublicationType type, List<String> authorIDs, String id)
			throws LiteratureDatabaseException {
		if(title == null || title.equals("")){
			throw new LiteratureDatabaseException("Title must not be empty.");
		}
		if(type == null){
			throw new LiteratureDatabaseException("Type must not be null.");
		}
		if (authorIDs == null || authorIDs.isEmpty()) {
			throw new LiteratureDatabaseException("authorIDs must not be empty.");
		}
		if(id == null || id.equals("")){
			throw new LiteratureDatabaseException("id must not be empty.");
		}
		if(yearPublished < 0){
			throw new LiteratureDatabaseException("Publication year must be greater than zero.");
		}
		List<Author> authors = new ArrayList<>();
		for(String i : authorIDs){
			if(!ValidationHelper.isId(i)){
				throw new LiteratureDatabaseException("Invalid Author id: " + i);
			}
			Author author = database.getAuthors().stream().filter(a -> a.getId().equals(i)).findAny().orElse(null);
			if(author == null){
				throw new LiteratureDatabaseException("Author not in Author list.");
			}else if(authors.contains(author)){
				throw new LiteratureDatabaseException("Author already in author list for publication.");
			} else {
				authors.add(author);
			}
		}
		if(!ValidationHelper.isId(id)){
			throw new LiteratureDatabaseException("Invalid Publication id.");
		}
		for(Publication p : database.getPublications()){
			if(p.getId().equals(id)){
				throw new LiteratureDatabaseException("Publication id is not unique.");
			}
		}
		Publication publication = new Publication();
		publication.setTitle(title);
		publication.setYearPublished(yearPublished);
		publication.setType(type);
		publication.setAuthors(authors);
		publication.setId(id);
		database.getPublications().add(publication);
	}

	@Override
	public void removePublicationByID(String id) throws LiteratureDatabaseException {
		if(id == null || id.equals("") || !ValidationHelper.isId(id)){
			throw new LiteratureDatabaseException("Invalid Publication id.");
		}
		if(database.getPublications().stream().noneMatch(publication -> publication.getId().equals(id))){
			throw new LiteratureDatabaseException("Publication id not found, not removing anything.");
		}
		database.getPublications().removeIf(publication -> publication.getId().equals(id));
	}

	@Override
	public void removeAuthorByID(String id) throws LiteratureDatabaseException {
		if(id == null || id.equals("") || !ValidationHelper.isId(id)){
			throw new LiteratureDatabaseException("Invalid Author id.");
		}
		if(database.getAuthors().stream().noneMatch(author -> author.getId().equals(id))){
			throw new LiteratureDatabaseException("Author id not found, not removing anything.");
		}
		database.getAuthors().removeIf(author -> author.getId().equals(id));
	}

	@Override
	public void addAuthor(String name, String email, String id) throws LiteratureDatabaseException {
		if(name == null || name.equals("")){
			throw new LiteratureDatabaseException("Invalid Author name.");
		}
		if(email == null || email.equals("") || !ValidationHelper.isEmail(email)){
			throw new LiteratureDatabaseException("Invalid Email.");
		}
		if(id == null || id.equals("") || !ValidationHelper.isId(id)){
			throw new LiteratureDatabaseException("Invalid id.");
		}
		for(Author author : database.getAuthors()){
			if(author.getId().equals(id)){
				throw new LiteratureDatabaseException("Author id is not unique.");
			}
		}
		Author author = new Author();
		author.setName(name);
		author.setEmail(email);
		author.setId(id);
		database.getAuthors().add(author);
	}

	@Override
	public List<Publication> getPublications() {
		return database.getPublications();
	}

	@Override
	public List<Author> getAuthors() {
		return database.getAuthors();
	}

	@Override
	public void clear() {
		database = new Database();
	}

	@Override
	public void printXMLToConsole() throws LiteratureDatabaseException {
		try {
			JAXBContext context = JAXBContext.newInstance(Database.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(database, System.out);
		}catch(JAXBException j){
			throw new LiteratureDatabaseException("Error during marshalling.", j.getCause());
		}
	}

	@Override
	public void saveXMLToFile(String path) throws LiteratureDatabaseException {
		try {
			JAXBContext context = JAXBContext.newInstance(Database.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(database, new File(path));
		}catch(JAXBException j){
			throw new LiteratureDatabaseException("Error during marshalling.", j.getCause());
		}
	}

}
