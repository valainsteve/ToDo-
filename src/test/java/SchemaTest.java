import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class SchemaTest {

    private Validator validator;

    @BeforeEach
    public void setUp() throws SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File("schema1.xsd"));
        validator = schema.newValidator();
    }

    @Test
    @DisplayName("Test with correct database")
    public void testCorrectDB() throws IOException, SAXException {
        testDB("src/test/resources/database.xml");
    }

    @Test
    @DisplayName("Test with missing author email")
    public void testMissingEmail(){
        Assertions.assertThrows(SAXException.class, () -> testDB("src/test/resources/database_author_noemail.xml"));
    }

    @Test
    @DisplayName("Test without author name")
    public void testMissingAuthorName(){
        Assertions.assertThrows(SAXException.class, () -> testDB("src/test/resources/database_author_noname.xml"));
    }

    @Test
    @DisplayName("Test without author ID")
    public void testMissingAuthorID(){
        Assertions.assertThrows(SAXException.class, () -> testDB("src/test/resources/database_author_noid.xml"));
    }

    @Test
    @DisplayName("Test with multiple author ids")
    public void testMultipleAuthorIds(){
        Assertions.assertThrows(SAXException.class, () -> testDB("src/test/resources/database_author_multipleids.xml"));
    }

    @Test
    @DisplayName("Test with multiple author names")
    public void testMultipleAuthorNames(){
        Assertions.assertThrows(SAXException.class, () -> testDB("src/test/resources/database_author_multiplenames.xml"));
    }

    @Test
    @DisplayName("Test without publication type")
    public void testMissingPublicationType(){
        Assertions.assertThrows(SAXException.class, () -> testDB("src/test/resources/database_publication_notype.xml"));
    }

    @Test
    @DisplayName("Test with wrong publication type")
    public void testWrongPublicationType(){
        Assertions.assertThrows(SAXException.class, () -> testDB("src/test/resources/database_publication_wrongtype.xml"));
    }

    @Test
    @DisplayName("Test without publication year")
    public void testMissingPublicationYear(){
        Assertions.assertThrows(SAXException.class, () -> testDB("src/test/resources/database_publication_noyear.xml"));
    }

    @Test
    @DisplayName("Test with empty publication year")
    public void testEmptyPublicationYear(){
        Assertions.assertThrows(SAXException.class, () -> testDB("src/test/resources/database_publication_yearempty.xml"));
    }

    @Test
    @DisplayName("Test with string as publication year")
    public void testPublicationYearString(){
        Assertions.assertThrows(SAXException.class, () -> testDB("src/test/resources/database_publication_yearstring.xml"));
    }

    @Test
    @DisplayName("Test without publication author(s)")
    public void testPublicationAuthorsMissing(){
        Assertions.assertThrows(SAXException.class, () -> testDB("src/test/resources/database_publication_noauthors.xml"));
    }

    @Test
    @DisplayName("Test without publication id")
    public void testMissingPublicationID(){
        Assertions.assertThrows(SAXException.class, () -> testDB("src/test/resources/database_publication_noid.xml"));
    }

    @Test
    @DisplayName("Test with multiple publication ids")
    public void testMultiplePublicationIDs(){
        Assertions.assertThrows(SAXException.class, () -> testDB("src/test/resources/database_publication_multipleids.xml"));
    }

    @Test
    @DisplayName("Test without publication title")
    public void testMissingPublicationTitle(){
        Assertions.assertThrows(SAXException.class, () -> testDB("src/test/resources/database_publication_notitle.xml"));
    }

    @Test
    @DisplayName("Test with multiple publication titles")
    public void testMultiplePublicationTitles(){
        Assertions.assertThrows(SAXException.class, () -> testDB("src/test/resources/database_publication_multipletitles.xml"));
    }

    public void testDB(String path) throws IOException, SAXException {
        validator.validate(new StreamSource(new File(path)));
    }
}
