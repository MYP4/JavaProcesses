package accounting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SalaryRecordTest {

    @Test
    void testDefaultConstructor() {
        SalaryRecord record = new SalaryRecord();
        assertNull(record.getDepartment());
        assertNull(record.getFullName());
        assertEquals(0, record.getAmount());
    }

    @Test
    void testParameterizedConstructor() {
        SalaryRecord record = new SalaryRecord("IT", "John Doe", 50000);
        assertEquals("IT", record.getDepartment());
        assertEquals("John Doe", record.getFullName());
        assertEquals(50000, record.getAmount());
    }

    @Test
    void testSettersAndGetters() {
        SalaryRecord record = new SalaryRecord();

        record.setDepartment("HR");
        assertEquals("HR", record.getDepartment());

        record.setFullName("Jane Smith");
        assertEquals("Jane Smith", record.getFullName());

        record.setAmount(75000);
        assertEquals(75000, record.getAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        SalaryRecord record1 = new SalaryRecord("IT", "John Doe", 50000);
        SalaryRecord record2 = new SalaryRecord("IT", "John Doe", 50000);
        SalaryRecord record3 = new SalaryRecord("HR", "Jane Smith", 60000);

        // Рефлексивность
        assertEquals(record1, record1);

        // Симметричность и транзитивность
        assertEquals(record1, record2);
        assertEquals(record2, record1);

        // Разные объекты
        assertNotEquals(record1, record3);
        assertNotEquals(record2, record3);

        // HashCode контракт
        assertEquals(record1.hashCode(), record2.hashCode());
        assertNotEquals(record1.hashCode(), record3.hashCode());

        // Сравнение с null
        assertNotEquals(null, record1);

        // Сравнение с другим типом
        assertNotEquals(record1, "string");
    }

    @Test
    void testToString() {
        SalaryRecord record = new SalaryRecord("IT", "John Doe", 50000);
        String toString = record.toString();

        assertTrue(toString.contains("IT"));
        assertTrue(toString.contains("John Doe"));
        assertTrue(toString.contains("50000"));
        assertTrue(toString.contains("department"));
        assertTrue(toString.contains("fullName"));
        assertTrue(toString.contains("amount"));
    }

    @Test
    void testWithNullValues() {
        SalaryRecord record = new SalaryRecord(null, null, 0);
        assertNull(record.getDepartment());
        assertNull(record.getFullName());
        assertEquals(0, record.getAmount());

        record.setDepartment(null);
        record.setFullName(null);
        assertNull(record.getDepartment());
        assertNull(record.getFullName());
    }

    @Test
    void testWithEmptyStrings() {
        SalaryRecord record = new SalaryRecord("", "", 0);
        assertEquals("", record.getDepartment());
        assertEquals("", record.getFullName());

        record.setDepartment("");
        record.setFullName("");
        assertEquals("", record.getDepartment());
        assertEquals("", record.getFullName());
    }

    @Test
    void testWithNegativeAmount() {
        SalaryRecord record = new SalaryRecord("IT", "John Doe", -1000);
        assertEquals(-1000, record.getAmount());

        record.setAmount(-5000);
        assertEquals(-5000, record.getAmount());
    }

    @Test
    void testWithMaxAmount() {
        SalaryRecord record = new SalaryRecord("IT", "John Doe", Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, record.getAmount());

        record.setAmount(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, record.getAmount());
    }
}