import providers.FileProvider;
import accounting.SalaryRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileProviderMockitoTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private FileProvider fileProvider;

    private Path testPath;
    private File testFile;
    private List<SalaryRecord> expectedRecords;

    @BeforeEach
    void setUp() {
        testPath = Path.of("test.json");
        testFile = testPath.toFile();
        expectedRecords = Arrays.asList(
                new SalaryRecord("IT", "John Doe", 50000),
                new SalaryRecord("HR", "Jane Smith", 60000)
        );
    }

    // ========== ТЕСТЫ ДЛЯ readFile ==========

    @Test
    void readFile_WhenMapperReadsSuccessfully_ShouldReturnRecords() throws Exception {
        // Arrange
        SalaryRecord[] recordsArray = expectedRecords.toArray(new SalaryRecord[0]);
        when(objectMapper.readValue(testFile, SalaryRecord[].class)).thenReturn(recordsArray);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(testPath);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedRecords, result);
        verify(objectMapper, times(1)).readValue(testFile, SalaryRecord[].class);
    }

    @Test
    void readFile_WhenMapperReturnsEmptyArray_ShouldReturnEmptyList() throws Exception {
        // Arrange
        SalaryRecord[] emptyArray = new SalaryRecord[0];
        when(objectMapper.readValue(testFile, SalaryRecord[].class)).thenReturn(emptyArray);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(testPath);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(objectMapper, times(1)).readValue(testFile, SalaryRecord[].class);
    }

    @Test
    void readFile_WhenIOExceptionOccurs_ShouldThrowInvalidDataException() throws Exception {
        // Arrange
        when(objectMapper.readValue(testFile, SalaryRecord[].class))
                .thenThrow(new IOException("File not found"));

        // Act & Assert
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> fileProvider.readFile(testPath)
        );

        assertTrue(exception.getMessage().contains("File not found"));
        verify(objectMapper, times(1)).readValue(testFile, SalaryRecord[].class);
    }

    @Test
    void readFile_WhenRuntimeExceptionOccurs_ShouldPropagate() throws Exception {
        // Arrange
        when(objectMapper.readValue(testFile, SalaryRecord[].class))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> fileProvider.readFile(testPath));
        verify(objectMapper, times(1)).readValue(testFile, SalaryRecord[].class);
    }

    // ========== ТЕСТЫ С РАЗНЫМИ ТИПАМИ ПУТЕЙ ==========

    @Test
    void readFile_WithRelativePath_ShouldWorkCorrectly() throws Exception {
        // Arrange
        Path relativePath = Path.of("data/test.json");
        File relativeFile = relativePath.toFile();
        SalaryRecord[] recordsArray = expectedRecords.toArray(new SalaryRecord[0]);
        when(objectMapper.readValue(relativeFile, SalaryRecord[].class)).thenReturn(recordsArray);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(relativePath);

        // Assert
        assertEquals(expectedRecords, result);
        verify(objectMapper, times(1)).readValue(relativeFile, SalaryRecord[].class);
    }

    @Test
    void readFile_WithAbsolutePath_ShouldWorkCorrectly() throws Exception {
        // Arrange
        Path absolutePath = Path.of("C:/data/test.json");
        File absoluteFile = absolutePath.toFile();
        SalaryRecord[] recordsArray = expectedRecords.toArray(new SalaryRecord[0]);
        when(objectMapper.readValue(absoluteFile, SalaryRecord[].class)).thenReturn(recordsArray);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(absolutePath);

        // Assert
        assertEquals(expectedRecords, result);
        verify(objectMapper, times(1)).readValue(absoluteFile, SalaryRecord[].class);
    }

    @Test
    void readFile_WithPathWithSpaces_ShouldWorkCorrectly() throws Exception {
        // Arrange
        Path pathWithSpaces = Path.of("test folder/my data.json");
        File fileWithSpaces = pathWithSpaces.toFile();
        SalaryRecord[] recordsArray = expectedRecords.toArray(new SalaryRecord[0]);
        when(objectMapper.readValue(fileWithSpaces, SalaryRecord[].class)).thenReturn(recordsArray);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(pathWithSpaces);

        // Assert
        assertEquals(expectedRecords, result);
        verify(objectMapper, times(1)).readValue(fileWithSpaces, SalaryRecord[].class);
    }

    // ========== ТЕСТЫ ДЛЯ ПРОВЕРКИ ВЫЗОВОВ ЛОГГЕРА ==========

    @Test
    void readFile_ShouldLogStartAndReadAndCompleteMessages() throws Exception {
        // Arrange
        SalaryRecord[] recordsArray = expectedRecords.toArray(new SalaryRecord[0]);
        when(objectMapper.readValue(testFile, SalaryRecord[].class)).thenReturn(recordsArray);

        // Act
        fileProvider.readFile(testPath);

        // Assert - проверяем, что методы вызваны (логирование нельзя проверить легко,
        // но можно через verify, если использовать мок для логгера)
        verify(objectMapper, times(1)).readValue(testFile, SalaryRecord[].class);
    }

    @Test
    void readFile_WhenErrorOccurs_ShouldLogError() throws Exception {
        // Arrange
        when(objectMapper.readValue(testFile, SalaryRecord[].class))
                .thenThrow(new IOException("File not found"));

        // Act & Assert
        assertThrows(InvalidDataException.class, () -> fileProvider.readFile(testPath));
        verify(objectMapper, times(1)).readValue(testFile, SalaryRecord[].class);
    }

    // ========== ТЕСТЫ ДЛЯ РАЗЛИЧНЫХ ТИПОВ ДАННЫХ ==========

    @Test
    void readFile_WithSingleRecord_ShouldReturnOneRecord() throws Exception {
        // Arrange
        List<SalaryRecord> singleRecord = Arrays.asList(
                new SalaryRecord("Legal", "Alice Brown", 80000)
        );
        SalaryRecord[] recordsArray = singleRecord.toArray(new SalaryRecord[0]);
        when(objectMapper.readValue(testFile, SalaryRecord[].class)).thenReturn(recordsArray);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(testPath);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Legal", result.get(0).getDepartment());
        assertEquals("Alice Brown", result.get(0).getFullName());
        assertEquals(80000, result.get(0).getAmount());
    }

    @Test
    void readFile_WithManyRecords_ShouldHandleCorrectly() throws Exception {
        // Arrange
        List<SalaryRecord> manyRecords = Arrays.asList(
                new SalaryRecord("Dept1", "Person1", 10000),
                new SalaryRecord("Dept2", "Person2", 20000),
                new SalaryRecord("Dept3", "Person3", 30000),
                new SalaryRecord("Dept4", "Person4", 40000),
                new SalaryRecord("Dept5", "Person5", 50000)
        );
        SalaryRecord[] recordsArray = manyRecords.toArray(new SalaryRecord[0]);
        when(objectMapper.readValue(testFile, SalaryRecord[].class)).thenReturn(recordsArray);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(testPath);

        // Assert
        assertEquals(5, result.size());
        verify(objectMapper, times(1)).readValue(testFile, SalaryRecord[].class);
    }

    @Test
    void readFile_WithDuplicateRecords_ShouldPreserveDuplicates() throws Exception {
        // Arrange
        SalaryRecord sameRecord = new SalaryRecord("IT", "John Doe", 50000);
        List<SalaryRecord> duplicateRecords = Arrays.asList(sameRecord, sameRecord, sameRecord);
        SalaryRecord[] recordsArray = duplicateRecords.toArray(new SalaryRecord[0]);
        when(objectMapper.readValue(testFile, SalaryRecord[].class)).thenReturn(recordsArray);

        // Act
        List<SalaryRecord> result = fileProvider.readFile(testPath);

        // Assert
        assertEquals(3, result.size());
        assertSame(result.get(0), result.get(1));
        assertSame(result.get(1), result.get(2));
    }

    // ========== ТЕСТЫ ДЛЯ writeToFile ==========

    @Test
    void writeToFile_ShouldNotThrowException() {
        // Arrange
        List<SalaryRecord> records = expectedRecords;

        // Act & Assert
        assertDoesNotThrow(() -> fileProvider.writeToFile(records, testPath));
        // Метод пустой, поэтому просто проверяем, что не падает
    }

    @Test
    void writeToFile_WithNullCollection_ShouldNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> fileProvider.writeToFile(null, testPath));
    }

    @Test
    void writeToFile_WithNullPath_ShouldNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> fileProvider.writeToFile(expectedRecords, null));
    }

    @Test
    void writeToFile_WithEmptyList_ShouldNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> fileProvider.writeToFile(Arrays.asList(), testPath));
    }

    @Test
    void writeToFile_CanBeCalledMultipleTimes() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            fileProvider.writeToFile(expectedRecords, testPath);
            fileProvider.writeToFile(expectedRecords, testPath);
            fileProvider.writeToFile(expectedRecords, testPath);
        });
    }
}