package accounting;

import lombok.Data;

@Data
public class SalaryRecord {
    private String department;
    private String fullName;
    private int amount;

    public SalaryRecord() {
    }

    public SalaryRecord(String department, String fullName, int amount) {
        this.department = department;
        this.fullName = fullName;
        this.amount = amount;
    }
}