package providers;

import accounting.SalaryRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.InvalidDataException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Provides file operations for reading and
 * writing salary records to JSON files.
 */
public final class FileProvider implements JsonReader, JsonWriter {
    /** Logger for the FileProvider class. */
    private static final Logger LOGGER =
            Logger.getLogger(FileProvider.class.getName());

    /** Object mapper for JSON serialization/deserialization. */
    private final ObjectMapper objectMapper;

    /**
     * Constructs a FileProvider with the specified object mapper.
     *
     * @param mapper the ObjectMapper to use for JSON processing
     */
    public FileProvider(final ObjectMapper mapper) {
        this.objectMapper = mapper;
    }

    /**
     * Default constructor that creates a default ObjectMapper.
     */
    public FileProvider() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Reads salary records from a JSON file.
     *
     * @param fileName the path to the file to read
     * @return a list of SalaryRecord objects
     * @throws InvalidDataException
     * if an I/O error occurs or data is invalid
     */
    @Override
    public List<SalaryRecord> readFile(final Path fileName)
            throws InvalidDataException {
        LOGGER.info("Starting the data reading process");
        try {
            LOGGER.info("Reading data");
            SalaryRecord[] records = objectMapper.readValue(
                    fileName.toFile(), SalaryRecord[].class);
            LOGGER.info("The data has been read");
            return Arrays.asList(records);

        } catch (IOException e) {
            LOGGER.severe("Error parsing data from file: "
                    + e.getMessage());
            throw new InvalidDataException(e.getMessage());
        }
    }

    /**
     * Writes salary records to a JSON file.
     *
     * @param collection the list of salary records to write
     * @param fileName the path to the file to write to
     */
    @Override
    public void writeToFile(final List<SalaryRecord> collection,
                            final Path fileName) {
        // In this task, implementation is not important
    }
}
