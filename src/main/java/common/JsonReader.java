package common;

import accounting.SalaryRecord;

import java.nio.file.Path;
import java.util.List;

public interface JsonReader {
    List<SalaryRecord> readFile(Path fileName) throws Exception;
}