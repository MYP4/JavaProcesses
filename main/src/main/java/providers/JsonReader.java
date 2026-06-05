package providers;

import accounting.SalaryRecord;
import exceptions.InvalidDataException;

import java.nio.file.Path;
import java.util.List;

/**
 * Interface for reading salary records from JSON files.
 */
@FunctionalInterface
public interface JsonReader {

    /**
     * Reads salary records from a JSON file.
     *
     * @param fileName the path to the file to read
     * @return a list of SalaryRecord objects
     * @throws InvalidDataException if an I/O error occurs or data is invalid
     */
    List<SalaryRecord> readFile(Path fileName) throws InvalidDataException;
}
