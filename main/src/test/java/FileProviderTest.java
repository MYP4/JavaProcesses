import providers.FileProvider;
import accounting.SalaryRecord;
import exceptions.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileProviderTest {

    private FileProvider fileProvider;

    @TempDir
    Path tempDir;  // Временная папка, автоматически очищается после каждого теста

    @BeforeEach
    void setUp() {
        fileProvider = new FileProvider();
    }

    // ========== ПОЗИТИВНЫЕ ТЕСТЫ ==========

    @Test
    void readFile_WithValidJson_ShouldReturnRecords() throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("test.json");
        String json = """
                [
                    {"department":"IT","fullName":"John Doe","amount":50000},
                    {"department":"HR","fullName":"Jane Smith","amount":60000},
                    {"department":"Finance","fullName":"Bob Johnson","amount":70000}
                ]
                """;
        Files.writeString(testFile, json);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(testFile);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("IT", result.get(0).getDepartment());
        assertEquals("John Doe", result.get(0).getFullName());
        assertEquals(50000, result.get(0).getAmount());
        assertEquals("HR", result.get(1).getDepartment());
        assertEquals("Jane Smith", result.get(1).getFullName());
        assertEquals(60000, result.get(1).getAmount());
        assertEquals("Finance", result.get(2).getDepartment());
        assertEquals("Bob Johnson", result.get(2).getFullName());
        assertEquals(70000, result.get(2).getAmount());
    }

    @Test
    void readFile_WithSingleRecord_ShouldReturnOneRecord() throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("single.json");
        String json = "[{\"department\":\"IT\",\"fullName\":\"John Doe\",\"amount\":50000}]";
        Files.writeString(testFile, json);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(testFile);

        // Assert
        assertEquals(1, result.size());
        assertEquals("IT", result.get(0).getDepartment());
        assertEquals("John Doe", result.get(0).getFullName());
        assertEquals(50000, result.get(0).getAmount());
    }

    @Test
    void readFile_WithEmptyArray_ShouldReturnEmptyList() throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("empty.json");
        Files.writeString(testFile, "[]");

        // Act
        List<SalaryRecord> result = fileProvider.readFile(testFile);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ========== ТЕСТЫ С РАЗНЫМИ ТИПАМИ ДАННЫХ ==========

    @Test
    void readFile_WithLargeNumbers_ShouldHandleCorrectly() throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("large.json");
        String json = "[{\"department\":\"IT\",\"fullName\":\"John Doe\",\"amount\":2147483647}]";
        Files.writeString(testFile, json);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(testFile);

        // Assert
        assertEquals(Integer.MAX_VALUE, result.get(0).getAmount());
    }

    @Test
    void readFile_WithNullValues_ShouldPreserveNulls() throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("nulls.json");
        String json = "[{\"department\":null,\"fullName\":null,\"amount\":0}]";
        Files.writeString(testFile, json);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(testFile);

        // Assert
        assertNull(result.get(0).getDepartment());
        assertNull(result.get(0).getFullName());
        assertEquals(0, result.get(0).getAmount());
    }

    @Test
    void readFile_WithEmptyStrings_ShouldPreserveEmptyStrings() throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("empty_strings.json");
        String json = "[{\"department\":\"\",\"fullName\":\"\",\"amount\":0}]";
        Files.writeString(testFile, json);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(testFile);

        // Assert
        assertEquals("", result.get(0).getDepartment());
        assertEquals("", result.get(0).getFullName());
    }

    @Test
    void readFile_WithUTF8Characters_ShouldHandleCorrectly() throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("utf8.json");
        String json = "[{\"department\":\"IT\",\"fullName\":\"Иван Петров\",\"amount\":50000}]";
        Files.writeString(testFile, json);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(testFile);

        // Assert
        assertEquals("Иван Петров", result.get(0).getFullName());
    }

    @Test
    void readFile_WithSpecialCharactersInNames_ShouldHandleCorrectly() throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("special.json");
        String json = "[{\"department\":\"IT\",\"fullName\":\"John O'Brian\",\"amount\":50000}]";
        Files.writeString(testFile, json);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(testFile);

        // Assert
        assertEquals("John O'Brian", result.get(0).getFullName());
    }

    // ========== НЕГАТИВНЫЕ ТЕСТЫ (ОШИБКИ) ==========

    @Test
    void readFile_WhenFileDoesNotExist_ShouldThrowInvalidDataException() {
        // Arrange
        Path nonExistentFile = tempDir.resolve("nonexistent.json");

        // Act & Assert
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> fileProvider.readFile(nonExistentFile)
        );
        assertTrue(exception.getMessage().contains("nonexistent.json"));
    }

    @Test
    void readFile_WhenFileIsDirectory_ShouldThrowInvalidDataException() throws Exception {
        // Arrange
        Path directory = tempDir.resolve("testDir");
        Files.createDirectory(directory);

        // Act & Assert
        assertThrows(InvalidDataException.class, () -> fileProvider.readFile(directory));
    }

    @Test
    void readFile_WithMalformedJson_ShouldThrowInvalidDataException() throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("malformed.json");
        Files.writeString(testFile, "{invalid json}");

        // Act & Assert
        assertThrows(InvalidDataException.class, () -> fileProvider.readFile(testFile));
    }

    @Test
    void readFile_WithExtraFields_ShouldThrowException() throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("extra_fields.json");
        String json = "[{\"department\":\"IT\",\"fullName\":\"John Doe\",\"amount\":50000,\"extra\":\"ignored\"}]";
        Files.writeString(testFile, json);

        // Act & Assert
        // Jackson по умолчанию НЕ игнорирует неизвестные поля
        assertThrows(InvalidDataException.class, () -> fileProvider.readFile(testFile));
    }

    @Test
    void readFile_WithWrongFieldType_ShouldThrowInvalidDataException() throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("wrong_type.json");
        String json = "[{\"department\":\"IT\",\"fullName\":\"John Doe\",\"amount\":\"not a number\"}]";
        Files.writeString(testFile, json);

        // Act & Assert
        assertThrows(InvalidDataException.class, () -> fileProvider.readFile(testFile));
    }

    @Test
    void readFile_WithNullPath_ShouldThrowException() {
        // Act & Assert
        assertThrows(Exception.class, () -> fileProvider.readFile(null));
    }

    // ========== ТЕСТЫ С БОЛЬШИМИ ОБЪЕМАМИ ДАННЫХ ==========

    @Test
    void readFile_WithManyRecords_ShouldHandleCorrectly() throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("many.json");
        int recordCount = 1000;
        StringBuilder jsonBuilder = new StringBuilder("[");
        for (int i = 0; i < recordCount; i++) {
            if (i > 0) jsonBuilder.append(",");
            jsonBuilder.append(String.format(
                    "{\"department\":\"Dept%d\",\"fullName\":\"Person%d\",\"amount\":%d}",
                    i % 10, i, 30000 + i
            ));
        }
        jsonBuilder.append("]");
        Files.writeString(testFile, jsonBuilder.toString());

        // Act
        List<SalaryRecord> result = fileProvider.readFile(testFile);

        // Assert
        assertEquals(recordCount, result.size());
        assertEquals("Dept0", result.get(0).getDepartment());
        assertEquals("Person0", result.get(0).getFullName());
        assertEquals(30000, result.get(0).getAmount());
        assertEquals("Dept9", result.get(9).getDepartment());
        assertEquals("Person999", result.get(999).getFullName());
        assertEquals(30999, result.get(999).getAmount());
    }

    @Test
    void readFile_WithDuplicateRecords_ShouldPreserveDuplicates() throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("duplicates.json");
        String json = """
                [
                    {"department":"IT","fullName":"John Doe","amount":50000},
                    {"department":"IT","fullName":"John Doe","amount":50000},
                    {"department":"IT","fullName":"John Doe","amount":50000}
                ]
                """;
        Files.writeString(testFile, json);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(testFile);

        // Assert
        assertEquals(3, result.size());
        assertEquals(result.get(0), result.get(1));
        assertEquals(result.get(1), result.get(2));
    }

    // ========== ТЕСТЫ С РАЗНЫМИ ФОРМАТАМИ JSON ==========

    @Test
    void readFile_WithPrettyPrintedJson_ShouldHandleCorrectly() throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("pretty.json");
        String json = """
                [
                  {
                    "department": "IT",
                    "fullName": "John Doe",
                    "amount": 50000
                  }
                ]
                """;
        Files.writeString(testFile, json);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(testFile);

        // Assert
        assertEquals(1, result.size());
        assertEquals("IT", result.get(0).getDepartment());
    }

    @Test
    void readFile_WithWhitespaceInJson_ShouldHandleCorrectly() throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("whitespace.json");
        String json = "\n\t  [\n\t    {\"department\":\"IT\",\"fullName\":\"John Doe\",\"amount\":50000}\n\t  ]  \n\t";
        Files.writeString(testFile, json);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(testFile);

        // Assert
        assertEquals(1, result.size());
        assertEquals("IT", result.get(0).getDepartment());
    }

    // ========== ДОПОЛНИТЕЛЬНЫЕ ТЕСТЫ ДЛЯ ПОЛНОТЫ ПОКРЫТИЯ ==========

    @Test
    void readFile_WithEmptyFile_ShouldThrowException() throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("empty_file.json");
        Files.writeString(testFile, "");  // completely empty file

        // Act & Assert
        assertThrows(InvalidDataException.class, () -> fileProvider.readFile(testFile));
    }

    @Test
    void readFile_WithOnlyWhitespace_ShouldThrowException() throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("whitespace_only.json");
        Files.writeString(testFile, "   \n\t   ");

        // Act & Assert
        assertThrows(InvalidDataException.class, () -> fileProvider.readFile(testFile));
    }

    @Test
    void readFile_WithNegativeAmounts_ShouldPreserveNegatives() throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("negative.json");
        String json = "[{\"department\":\"IT\",\"fullName\":\"John Doe\",\"amount\":-5000}]";
        Files.writeString(testFile, json);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(testFile);

        // Assert
        assertEquals(-5000, result.get(0).getAmount());
    }

    @Test
    void readFile_WithZeroAmount_ShouldPreserveZero() throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("zero.json");
        String json = "[{\"department\":\"IT\",\"fullName\":\"John Doe\",\"amount\":0}]";
        Files.writeString(testFile, json);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(testFile);

        // Assert
        assertEquals(0, result.get(0).getAmount());
    }
}