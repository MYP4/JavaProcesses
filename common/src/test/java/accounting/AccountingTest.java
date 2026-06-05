package accounting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AccountingTest {
    private Accounting accounting;

    @BeforeEach
    void setUp() {
        List<SalaryRecord> records = Arrays.asList(  // Используем Arrays.asList для изменяемого списка
                new SalaryRecord("IT", "John Doe", 50000),
                new SalaryRecord("HR", "Jane Smith", 60000),
                new SalaryRecord("Finance", "Bob Johnson", 70000),
                new SalaryRecord("IT", "Alice Johnson", 55000),
                new SalaryRecord("Sales", "Michael Brown", 62000),
                new SalaryRecord("IT", "Emily Davis", 68000),
                new SalaryRecord("Legal", "David Miller", 75000),
                new SalaryRecord("Sales", "Samantha Wilson", 58000)
        );
        accounting = new Accounting(records);
    }

    @Test
    void testDefaultConstructor() {
        Accounting emptyAccounting = new Accounting();
        assertNull(emptyAccounting.getSalaryRecords());
    }

    @Test
    void testParameterizedConstructor() {
        List<SalaryRecord> records = new ArrayList<>();
        Accounting newAccounting = new Accounting(records);
        assertEquals(records, newAccounting.getSalaryRecords());
    }

    @Test
    void testSetSalaryRecords() {
        List<SalaryRecord> newRecords = new ArrayList<>();
        accounting.setSalaryRecords(newRecords);
        assertEquals(newRecords, accounting.getSalaryRecords());
    }

    @Test
    void testFindDepartmentWithHighestAverageSalary_WhenOneDepartment() {
        List<SalaryRecord> singleDepartment = Arrays.asList(
                new SalaryRecord("IT", "John Doe", 50000),
                new SalaryRecord("IT", "Jane Smith", 60000)
        );
        accounting.setSalaryRecords(singleDepartment);
        String result = accounting.findDepartmentWithHighestAverageSalary();
        assertEquals("IT", result);
    }

    @Test
    void testFindDepartmentWithHighestAverageSalary_WhenSameAverageSalary() {
        List<SalaryRecord> sameAverage = Arrays.asList(
                new SalaryRecord("IT", "John Doe", 50000),
                new SalaryRecord("IT", "Jane Smith", 70000),
                new SalaryRecord("HR", "Bob Brown", 60000),
                new SalaryRecord("HR", "Alice White", 60000)
        );
        accounting.setSalaryRecords(sameAverage);
        String result = accounting.findDepartmentWithHighestAverageSalary();
        assertNotNull(result);
        assertTrue(result.equals("IT") || result.equals("HR"));
    }

    @Test
    void testFindDepartmentWithHighestTotalPayout_WhenOneDepartment() {
        List<SalaryRecord> singleDepartment = Arrays.asList(
                new SalaryRecord("Sales", "John Doe", 50000),
                new SalaryRecord("Sales", "Jane Smith", 60000)
        );
        accounting.setSalaryRecords(singleDepartment);
        String result = accounting.findDepartmentWithHighestTotalPayout();
        assertEquals("Sales", result);
    }

    @Test
    void testFindDepartmentWithHighestTotalPayout_WhenSameTotal() {
        List<SalaryRecord> sameTotal = Arrays.asList(
                new SalaryRecord("IT", "John Doe", 60000),
                new SalaryRecord("IT", "Jane Smith", 40000),
                new SalaryRecord("HR", "Bob Brown", 50000),
                new SalaryRecord("HR", "Alice White", 50000)
        );
        accounting.setSalaryRecords(sameTotal);
        String result = accounting.findDepartmentWithHighestTotalPayout();
        assertNotNull(result);
        assertTrue(result.equals("IT") || result.equals("HR"));
    }

    @Test
    void testEmptyListWithParameterizedConstructor() {
        Accounting emptyAccounting = new Accounting(new ArrayList<>());
        assertTrue(emptyAccounting.groupTheRecordsByDepartments().isEmpty());
        assertNull(emptyAccounting.findDepartmentWithHighestAverageSalary());
        assertNull(emptyAccounting.findDepartmentWithHighestTotalPayout());
    }

    @Test
    void testWithSingleRecord() {
        List<SalaryRecord> singleRecord = List.of(
                new SalaryRecord("Legal", "John Doe", 100000)
        );
        accounting.setSalaryRecords(singleRecord);

        Map<String, List<SalaryRecord>> grouped = accounting.groupTheRecordsByDepartments();
        assertEquals(1, grouped.size());
        assertEquals(1, grouped.get("Legal").size());

        String highestAvg = accounting.findDepartmentWithHighestAverageSalary();
        assertEquals("Legal", highestAvg);

        String highestTotal = accounting.findDepartmentWithHighestTotalPayout();
        assertEquals("Legal", highestTotal);
    }

    @Test
    void testWithNegativeSalary() {
        List<SalaryRecord> negativeSalary = Arrays.asList(
                new SalaryRecord("IT", "John Doe", -1000),
                new SalaryRecord("HR", "Jane Smith", 1000)
        );
        accounting.setSalaryRecords(negativeSalary);

        String highestAvg = accounting.findDepartmentWithHighestAverageSalary();
        assertEquals("HR", highestAvg);

        String highestTotal = accounting.findDepartmentWithHighestTotalPayout();
        assertEquals("HR", highestTotal);
    }
}