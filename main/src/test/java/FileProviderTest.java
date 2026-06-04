import accounting.SalaryRecord;
import exceptions.InvalidDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import providers.FileProvider;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileProviderTest {
    private static final String GOOD_FILE = "src/test/resources/Good.json";
    private static final String BAD_FILE = "src/test/resources/Bad.json";
    private static final String EMPTY_FILE = "src/test/resources/Empty.json";

    private final FileProvider fileProvider = new FileProvider();

    @Test
    void readGoodFileTest() throws InvalidDataException {

        var records = List.of(
                new SalaryRecord("IT", "John Doe", 50000),
                new SalaryRecord("HR", "Jane Smith", 60000),
                new SalaryRecord("Finance", "Bob Johnson", 70000)
        );

        var recordsFromFile = fileProvider.readFile(Path.of(GOOD_FILE));

        for (var i = 0; i < recordsFromFile.size(); i++) {
            assertEquals(records.get(i), recordsFromFile.get(i));
        }
    }

    @Test
    void readBadFileTest()  {
        Assertions.assertThrows(InvalidDataException.class, () -> fileProvider.readFile(Path.of(BAD_FILE)));
    }

    @Test
    void readEmptyFileTest() throws  InvalidDataException{
        var recordsFromFile = fileProvider.readFile(Path.of(EMPTY_FILE));

        assertEquals(0, recordsFromFile.size());
    }
}