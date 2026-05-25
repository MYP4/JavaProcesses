import accounting.Accounting;
import common.FileProvider;

import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Logger;

/*
Дан файл, содержащий сведения о заработной плате сотрудников предприятия в формате:
Номер отдела;ФИО;Сумма
Необходимо написать программу, которая позволит:
1. Распечатать ведомости по отделам
2. Найти отдел с самой высокой средней заработной платой
3. Найти отдел с самой большой общей суммой выплаты
В задаче должны использоваться элементы функционального программирования
Задача должна быть представлена в виде maven-проекта
Задача должна быть покрыта тестами с помощью JUnit
*/

public class Main {
    private static final String FILE_NAME = "salary_records.json";
    private static final Logger logger = Logger.getLogger(FileProvider.class.getName());

    public static void main(String[] args) {
        logger.info("The beginning of the demonstration work");

        var accounting = new Accounting();
        logger.info("Created \"Accounting\"");
        var fileProvider = new FileProvider();
        logger.info("Created \"FileProvider\"");

        try {
            var path = Paths.get(Objects.requireNonNull(Main.class.getResource(FILE_NAME)).toURI());
            logger.info("Created path to file");

            try {
                var records = fileProvider.readFile(path);
                accounting.setSalaryRecords(records);

            } catch (Exception ex) {
                logger.severe(ex.getMessage());
            }

            logger.info("Demonstration of how the methods work");

            accounting.groupTheRecordsByDepartments();

            var averageSalary = accounting.findDepartmentWithHighestAverageSalary();
            logger.info("Department with highest average salary is " + averageSalary);

            var totalSalary = accounting.findDepartmentWithHighestTotalPayout();
            logger.info("Department with highest highest total payout is " + totalSalary);

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
}