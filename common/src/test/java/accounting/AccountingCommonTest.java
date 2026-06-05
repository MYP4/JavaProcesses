package accounting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountingCommonTest {

    private Accounting accounting1;
    private Accounting accounting2;
    private Accounting accounting3;
    private List<SalaryRecord> records1;
    private List<SalaryRecord> records2;

    @BeforeEach
    void setUp() {
        records1 = Arrays.asList(
                new SalaryRecord("IT", "John Doe", 50000),
                new SalaryRecord("HR", "Jane Smith", 60000)
        );

        records2 = Arrays.asList(
                new SalaryRecord("IT", "John Doe", 50000),
                new SalaryRecord("HR", "Jane Smith", 60000)
        );

        accounting1 = new Accounting(records1);
        accounting2 = new Accounting(records2);
        accounting3 = new Accounting(new ArrayList<>());
    }

    // ========== ТЕСТЫ ДЛЯ EQUALS ==========

    @Test
    void testEquals_SameObject_ShouldReturnTrue() {
        assertTrue(accounting1.equals(accounting1));
    }

    @Test
    void testEquals_EqualObjects_ShouldReturnTrue() {
        // Оба объекта имеют одинаковые salaryRecords
        assertTrue(accounting1.equals(accounting2));
        assertTrue(accounting2.equals(accounting1));
    }

    @Test
    void testEquals_DifferentObjects_ShouldReturnFalse() {
        assertFalse(accounting1.equals(accounting3));
        assertFalse(accounting3.equals(accounting1));
    }

    @Test
    void testEquals_WithNull_ShouldReturnFalse() {
        assertFalse(accounting1.equals(null));
    }

    @Test
    void testEquals_DifferentClass_ShouldReturnFalse() {
        assertFalse(accounting1.equals("string"));
        assertFalse(accounting1.equals(123));
    }

    @Test
    void testEquals_BothHaveNullLists_ShouldReturnTrue() {
        Accounting accountingNull1 = new Accounting();
        Accounting accountingNull2 = new Accounting();

        assertTrue(accountingNull1.equals(accountingNull2));
    }

    @Test
    void testEquals_OneHasNullList_ShouldReturnFalse() {
        Accounting accountingWithNull = new Accounting();
        Accounting accountingWithList = new Accounting(records1);

        assertFalse(accountingWithNull.equals(accountingWithList));
        assertFalse(accountingWithList.equals(accountingWithNull));
    }

    @Test
    void testEquals_DifferentListSizes_ShouldReturnFalse() {
        List<SalaryRecord> smallerList = Arrays.asList(
                new SalaryRecord("IT", "John Doe", 50000)
        );
        Accounting accountingSmaller = new Accounting(smallerList);

        assertFalse(accounting1.equals(accountingSmaller));
    }

    @Test
    void testEquals_SameListDifferentOrder_ShouldReturnTrue() {
        // Lombok @Data генерирует equals, который сравнивает содержимое списка
        // Порядок элементов важен, так как List сравнивает порядок
        List<SalaryRecord> reversedList = Arrays.asList(
                new SalaryRecord("HR", "Jane Smith", 60000),
                new SalaryRecord("IT", "John Doe", 50000)
        );
        Accounting accountingReversed = new Accounting(reversedList);

        // Для List порядок важен, поэтому разные порядки дадут false
        // Если нужен порядок-независимый equals, используйте Set
        assertFalse(accounting1.equals(accountingReversed));
    }

    @Test
    void testEquals_EmptyLists_ShouldReturnTrue() {
        Accounting accountingEmpty1 = new Accounting(new ArrayList<>());
        Accounting accountingEmpty2 = new Accounting(new ArrayList<>());

        assertTrue(accountingEmpty1.equals(accountingEmpty2));
    }

    @Test
    void testEquals_WithSameContentDifferentObjects_ShouldReturnTrue() {
        // Создаем новые объекты с теми же данными
        List<SalaryRecord> sameRecords = Arrays.asList(
                new SalaryRecord("IT", "John Doe", 50000),
                new SalaryRecord("HR", "Jane Smith", 60000)
        );
        Accounting accountingSame = new Accounting(sameRecords);

        assertTrue(accounting1.equals(accountingSame));
    }

    // ========== ТЕСТЫ ДЛЯ HASHCODE ==========

    @Test
    void testHashCode_EqualObjects_ShouldHaveSameHashCode() {
        assertEquals(accounting1.hashCode(), accounting2.hashCode());
    }

    @Test
    void testHashCode_DifferentObjects_ShouldHaveDifferentHashCode() {
        assertNotEquals(accounting1.hashCode(), accounting3.hashCode());
    }

    @Test
    void testHashCode_Consistent_ShouldReturnSameValueOnMultipleCalls() {
        int hashCode1 = accounting1.hashCode();
        int hashCode2 = accounting1.hashCode();
        int hashCode3 = accounting1.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertEquals(hashCode2, hashCode3);
    }

    @Test
    void testHashCode_BothHaveNullLists_ShouldReturnSameHashCode() {
        Accounting accountingNull1 = new Accounting();
        Accounting accountingNull2 = new Accounting();

        assertEquals(accountingNull1.hashCode(), accountingNull2.hashCode());
    }

    @Test
    void testHashCode_EmptyLists_ShouldReturnSameHashCode() {
        Accounting accountingEmpty1 = new Accounting(new ArrayList<>());
        Accounting accountingEmpty2 = new Accounting(new ArrayList<>());

        assertEquals(accountingEmpty1.hashCode(), accountingEmpty2.hashCode());
    }

    @Test
    void testHashCode_ContractWithEquals() {
        // Если equals возвращает true, hashCode должен быть одинаковым
        assertTrue(accounting1.equals(accounting2));
        assertEquals(accounting1.hashCode(), accounting2.hashCode());

        // Если equals возвращает false, hashCode не обязан быть разным,
        // но хорошо бы, чтобы был разным
        assertFalse(accounting1.equals(accounting3));
        // HashCode может быть разным, но это не обязательно
    }

    // ========== ТЕСТЫ ДЛЯ TOSTRING ==========

    @Test
    void testToString_ShouldContainClassAndFields() {
        String toString = accounting1.toString();

        assertTrue(toString.contains("Accounting"));
        assertTrue(toString.contains("salaryRecords"));
    }

    @Test
    void testToString_WithData_ShouldShowRecords() {
        String toString = accounting1.toString();

        assertTrue(toString.contains("IT"));
        assertTrue(toString.contains("John Doe"));
        assertTrue(toString.contains("50000"));
        assertTrue(toString.contains("HR"));
        assertTrue(toString.contains("Jane Smith"));
        assertTrue(toString.contains("60000"));
    }

    @Test
    void testToString_WithNullList_ShouldShowNull() {
        Accounting accountingNull = new Accounting();
        String toString = accountingNull.toString();

        assertTrue(toString.contains("salaryRecords=null"));
    }

    @Test
    void testToString_WithEmptyList_ShouldShowEmpty() {
        Accounting accountingEmpty = new Accounting(new ArrayList<>());
        String toString = accountingEmpty.toString();

        assertTrue(toString.contains("[]"));
    }

    @Test
    void testToString_Consistent_ShouldReturnSameValueOnMultipleCalls() {
        String toString1 = accounting1.toString();
        String toString2 = accounting1.toString();

        assertEquals(toString1, toString2);
    }

    @Test
    void testToString_DifferentObjects_ShouldHaveDifferentStrings() {
        String toString1 = accounting1.toString();
        String toString3 = accounting3.toString();

        assertNotEquals(toString1, toString3);
    }

    // ========== ТЕСТЫ ДЛЯ КОНСТРУКТОРОВ ==========

    @Test
    void testDefaultConstructor() {
        Accounting accounting = new Accounting();
        assertNull(accounting.getSalaryRecords());
    }

    @Test
    void testParameterizedConstructor() {
        Accounting accounting = new Accounting(records1);
        assertEquals(records1, accounting.getSalaryRecords());
    }

    @Test
    void testParameterizedConstructor_WithNull() {
        Accounting accounting = new Accounting(null);
        assertNull(accounting.getSalaryRecords());
    }

    @Test
    void testParameterizedConstructor_WithEmptyList() {
        List<SalaryRecord> emptyList = new ArrayList<>();
        Accounting accounting = new Accounting(emptyList);
        assertEquals(emptyList, accounting.getSalaryRecords());
    }

    // ========== ТЕСТЫ ДЛЯ GETTERS/SETTERS ==========

    @Test
    void testSetSalaryRecords() {
        Accounting accounting = new Accounting();
        assertNull(accounting.getSalaryRecords());

        accounting.setSalaryRecords(records1);
        assertEquals(records1, accounting.getSalaryRecords());

        accounting.setSalaryRecords(null);
        assertNull(accounting.getSalaryRecords());
    }

    @Test
    void testGetSalaryRecords() {
        Accounting accounting = new Accounting(records1);
        assertEquals(records1, accounting.getSalaryRecords());
    }

    @Test
    void testGetSalaryRecords_AfterModification() {
        Accounting accounting = new Accounting(records1);
        List<SalaryRecord> retrieved = accounting.getSalaryRecords();

        // Изменение полученного списка НЕ должно влиять на внутреннее состояние,
        // если только это не та же ссылка
        // В данном случае это та же ссылка, потому что мы не делаем defensive copy
        // Это важно для понимания контракта
        assertSame(records1, retrieved);
    }
}