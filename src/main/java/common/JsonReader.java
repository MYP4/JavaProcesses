package common;

import accounting.SalaryRecord;
import exceptions.InvalidDataException;

import java.nio.file.Path;
import java.util.List;

public interface JsonReader {
    public List<SalaryRecord> readFile(Path fileName) throws InvalidDataException;
}