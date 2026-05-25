package common;

import accounting.SalaryRecord;

import java.nio.file.Path;
import java.util.List;

public interface JsonWriter {
    void writeToFile(List<SalaryRecord> collection, Path fileName);
}