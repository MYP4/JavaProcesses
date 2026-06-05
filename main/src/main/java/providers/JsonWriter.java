package providers;

import accounting.SalaryRecord;

import java.nio.file.Path;
import java.util.List;

/**
 * Interface for writing salary records to JSON files.
 */
@FunctionalInterface
public interface JsonWriter {

    /**
     * Writes salary records to a JSON file.
     *
     * @param collection the list of salary records to write
     * @param fileName the path to the file to write to
     */
    void writeToFile(List<SalaryRecord> collection, Path fileName);
}
