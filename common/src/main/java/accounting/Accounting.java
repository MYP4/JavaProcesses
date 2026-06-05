package accounting;

import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Provides accounting operations for salary records including grouping,
 * calculating average salaries, and total payouts by department.
 */
@Data
public final class Accounting {
    /** Logger for the Accounting class. */
    private static final Logger LOGGER =
            Logger.getLogger(Accounting.class.getName());

    /** List of salary records. */
    private List<SalaryRecord> salaryRecords;

    /**
     * Default constructor.
     */
    public Accounting() {
    }

    /**
     * Constructs an Accounting instance with the specified salary records.
     *
     * @param records the list of salary records to be processed
     */
    public Accounting(final List<SalaryRecord> records) {
        this.salaryRecords = records;
    }

    /**
     * Groups salary records by department.
     *
     * @return a map where the key is department name and the value is
     *         a list of salary records belonging to that department
     */
    public Map<String, List<SalaryRecord>> groupTheRecordsByDepartments() {
        LOGGER.info("The method \"groupTheRecordsByDepartments\" "
                + "has started working");

        return salaryRecords.stream()
                .collect(Collectors.groupingBy(
                        SalaryRecord::getDepartment
                ));
    }

    /**
     * Finds the department with the highest average salary.
     *
     * @return the name of the department with the highest average salary,
     *         or null if no records exist
     */
    public String findDepartmentWithHighestAverageSalary() {
        LOGGER.info("The method \"findDepartmentWithHighestAverageSalary\""
                + " has started working");

        return salaryRecords.stream()
                .collect(Collectors.groupingBy(
                        SalaryRecord::getDepartment,
                        Collectors.averagingInt(SalaryRecord::getAmount)
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Finds the department with the highest total payout.
     *
     * @return the name of the department with the highest total salary payout,
     *         or null if no records exist
     */
    public String findDepartmentWithHighestTotalPayout() {
        LOGGER.info("The method \"findDepartmentWithHighestTotalPayout\""
                + " has started working");

        return salaryRecords.stream()
                .collect(Collectors.groupingBy(
                        SalaryRecord::getDepartment,
                        Collectors.summingInt(SalaryRecord::getAmount)
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
