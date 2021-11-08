package de.uniba.wiai.dsg.ajp.assignment2.literature.logic.model;

import javax.xml.bind.annotation.*;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

public class Publication {

	private String id;
	private String title;
	private int yearPublished;
	private PublicationType type;
	private List<Author> authors = new LinkedList<>();

	public Publication() {
		super();
	}

	@XmlAttribute(required = true)
	public PublicationType getType() {
		return type;
	}

	public void setType(PublicationType type) {
		this.type = type;
	}

	@XmlAttribute(required = true)
	public int getYearPublished() {
		return yearPublished;
	}

	public void setYearPublished(int yearPublished) {
		this.yearPublished = yearPublished;
	}

	@XmlElement(required = true)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@XmlElement(required = true)
	@XmlID
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement(required = true, name = "author")
	@XmlIDREF
	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	@Override
	public String toString() {
		return String.format(
				"[%s] The author(s) %s published %s as a %s in %d", id,
				getAuthorNames(), title, type, yearPublished);
	}

	private String getAuthorNames() {
		StringJoiner result = new StringJoiner(", ");
		for (int i = 0; i < authors.size(); i++) {
			result.add(authors.get(i).getName());
		}
		return result.toString();
	}
}
