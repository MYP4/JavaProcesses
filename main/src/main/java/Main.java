import accounting.Accounting;
import providers.FileProvider;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

/**
 * Main entry point for the salary processing application.
 */
public final class Main {
    /** Name of the salary records resource file. */
    private static final String FILE_NAME = "salary_records.json";

    /** Logger for the Main class. */
    private static final Logger LOGGER =
            Logger.getLogger(Main.class.getName());

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private Main() {
        throw new UnsupportedOperationException(
                "Utility class cannot be instantiated");
    }

    /**
     * Main method to run the salary processing application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(final String[] args) {
        LOGGER.info("The beginning of the demonstration work");

        var accounting = new Accounting();
        LOGGER.info("Created \"Accounting\"");
        var fileProvider = new FileProvider();
        LOGGER.info("Created \"FileProvider\"");

        try {
            Path tempPath = createTempFileFromResource();
            LOGGER.info("Created temp file: " + tempPath);

            var records = fileProvider.readFile(tempPath);
            accounting.setSalaryRecords(records);

            LOGGER.info("Demonstration of how the methods work");

            accounting.groupTheRecordsByDepartments();

            var averageSalary =
                    accounting.findDepartmentWithHighestAverageSalary();
            LOGGER.info("Department with highest average salary is "
                    + averageSalary);

            var totalSalary =
                    accounting.findDepartmentWithHighestTotalPayout();
            LOGGER.info("Department with highest total payout is "
                    + totalSalary);

        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
    }

    /**
     * Creates a temporary file from a resource.
     *
     * @return the path to the created temporary file
     * @throws Exception if the resource cannot be found or copied
     */
    private static Path createTempFileFromResource() throws Exception {
        try (InputStream inputStream =
                     Main.class.getResourceAsStream("/" + Main.FILE_NAME)) {
            if (inputStream == null) {
                throw new Exception("Resource not found: " + Main.FILE_NAME);
            }

            Path tempFile = Files.createTempFile("salary_", ".tmp");
            Files.copy(inputStream, tempFile,
                    StandardCopyOption.REPLACE_EXISTING);
            tempFile.toFile().deleteOnExit();

            return tempFile;
        }
    }
}
