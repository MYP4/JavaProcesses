package accounting;

import lombok.Data;

/**
 * Represents a salary record for an employee.
 * Contains department name, employee full name, and salary amount.
 */
@Data
public final class SalaryRecord {
    /** Department name. */
    private String department;

    /** Employee full name. */
    private String fullName;

    /** Salary amount. */
    private int amount;

    /**
     * Default constructor.
     */
    public SalaryRecord() {
    }

    /**
     * Constructs a SalaryRecord with the specified department,
     * full name, and amount.
     *
     * @param dept the department where the employee works
     * @param name the full name of the employee
     * @param sal the salary amount
     */
    public SalaryRecord(final String dept, final String name,
                        final int sal) {
        this.department = dept;
        this.fullName = name;
        this.amount = sal;
    }
}
