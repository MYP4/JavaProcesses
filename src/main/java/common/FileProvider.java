package common;

import accounting.SalaryRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.InvalidDataException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class FileProvider implements JsonReader, JsonWriter {
    private static final Logger logger = Logger.getLogger(FileProvider.class.getName());
    private final ObjectMapper mapper = new ObjectMapper();

    public FileProvider() {}

    @Override
    public List<SalaryRecord> readFile(Path fileName) throws InvalidDataException {
        logger.info("Starting the data reading process");
        try {
            logger.info("Reading data");
            SalaryRecord[] records = mapper.readValue(fileName.toFile(), SalaryRecord[].class);
            logger.info("The data has been read");
            return Arrays.asList(records);

        } catch (IOException e) {
            logger.severe("\"Error parsing data from file\"" + e.getMessage());
            throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public void writeToFile(List<SalaryRecord> collection, Path fileName) {
        // В данной задаче реализация не важна
    }
}