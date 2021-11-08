package de.uniba.wiai.dsg.ajp.assignment2.literature.logic.impl;

import de.uniba.wiai.dsg.ajp.assignment2.literature.logic.DatabaseService;
import de.uniba.wiai.dsg.ajp.assignment2.literature.logic.LiteratureDatabaseException;
import de.uniba.wiai.dsg.ajp.assignment2.literature.logic.MainService;
import de.uniba.wiai.dsg.ajp.assignment2.literature.logic.model.Database;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class MainServiceImpl implements MainService {
	private static final String schemaPath = "schema1.xsd";

	/**
	 * Default constructor required for grading.
	 */
	public MainServiceImpl() {
		/*
		 * DO NOT REMOVE - REQUIRED FOR GRADING
		 * 
		 * YOU CAN EXTEND IT BELOW THIS COMMENT
		 */
	}

	@Override
	public void validate(String path) throws LiteratureDatabaseException {
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			Schema schema = schemaFactory.newSchema(new File(schemaPath));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new File(path)));
		}catch (IOException e) {
			throw new LiteratureDatabaseException("Error while reading/opening file!", e.getCause());
		} catch (SAXException s) {
			throw new LiteratureDatabaseException("Validation failed!", s.getCause());
		}

	}

	@Override
	public DatabaseService load(String path) throws LiteratureDatabaseException {
		validate(path);
		try {
			JAXBContext context = JAXBContext.newInstance(Database.class);
			Unmarshaller um = context.createUnmarshaller();
			Database database = (Database) um.unmarshal(new File(path));
			return new DatabaseServiceImpl(database);
		} catch (JAXBException j){
			throw new LiteratureDatabaseException("Error while marshaling file!", j.getCause());
		}
	}

	@Override
	public DatabaseService create() throws LiteratureDatabaseException {
		Database database = new Database();
		return new DatabaseServiceImpl(database);
	}

}
