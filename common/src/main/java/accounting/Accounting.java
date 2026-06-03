package accounting;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Data
public class Accounting {
    private static final Logger logger = Logger.getLogger(Accounting.class.getName());
    private List<SalaryRecord> salaryRecords;

    public Accounting() {
    }

    public Accounting(List<SalaryRecord> salaryRecords) {
        this.salaryRecords = salaryRecords;
    }


    public Map<String, List<SalaryRecord>> groupTheRecordsByDepartments(){
        logger.info("The method \"groupTheRecordsByDepartments\" has started working");

        return salaryRecords.stream()
                .collect(Collectors.groupingBy(
                        SalaryRecord::getDepartment
                ));
    }

    public String findDepartmentWithHighestAverageSalary() {
        logger.info("The method \"findDepartmentWithHighestAverageSalary\" has started working");

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

    public String findDepartmentWithHighestTotalPayout() {
        logger.info("The method \"findDepartmentWithHighestTotalPayout\" has started working");

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