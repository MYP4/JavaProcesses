import accounting.Accounting;
import providers.FileProvider;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.logging.Logger;

public class Main {
    private static final String FILE_NAME = "salary_records.json";
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        logger.info("The beginning of the demonstration work");

        var accounting = new Accounting();
        logger.info("Created \"Accounting\"");
        var fileProvider = new FileProvider();
        logger.info("Created \"FileProvider\"");

        try {
            // Изменение только здесь - создаем временный файл из ресурса
            Path tempPath = createTempFileFromResource(FILE_NAME);
            logger.info("Created temp file: " + tempPath);

            var records = fileProvider.readFile(tempPath);
            accounting.setSalaryRecords(records);

            logger.info("Demonstration of how the methods work");

            accounting.groupTheRecordsByDepartments();

            var averageSalary = accounting.findDepartmentWithHighestAverageSalary();
            logger.info("Department with highest average salary is " + averageSalary);

            var totalSalary = accounting.findDepartmentWithHighestTotalPayout();
            logger.info("Department with highest total payout is " + totalSalary);

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private static Path createTempFileFromResource(String resourceName) throws Exception {
        try (InputStream inputStream = Main.class.getResourceAsStream("/" + resourceName)) {
            if (inputStream == null) {
                throw new Exception("Resource not found: " + resourceName);
            }

            Path tempFile = Files.createTempFile("salary_", ".tmp");
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            tempFile.toFile().deleteOnExit();

            return tempFile;
        }
    }
}