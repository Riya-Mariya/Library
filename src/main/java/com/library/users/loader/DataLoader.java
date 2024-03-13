package com.library.users.loader;

import com.library.users.models.Books;
import com.library.users.models.Borrowed;
import com.library.users.models.DataSource;
import com.library.users.models.User;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
@Slf4j
public class DataLoader {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @Getter
    private final DataSource dataSource;

    public DataLoader() {
        this.dataSource = new DataSource();
        loadData(dataSource);
    }

    public void loadData(DataSource dataSource) {
        dataSource.setUsersDetails(loadUsersFromCSV());
        dataSource.setBooksDetails(loadBooksFromCSV());
        dataSource.setBorrowingsDetails(loadBorrowingsFromCSV());
    }

    private List<User> loadUsersFromCSV() {
        return loadFromCSV("src/main/resources/dataSource/user.csv",
                parts -> User.builder()
                        .name(parts[0])
                        .firstName(parts[1])
                        .memberSince(parseDate(parts[2]))
                        .memberTill(parseDate(parts[3]))
                        .gender(parseGender(parts[4]).orElse(Character.MIN_VALUE))
                        .build());
    }

    private List<Books> loadBooksFromCSV() {
        return loadFromCSV("src/main/resources/dataSource/books.csv",
                parts -> Books.builder()
                        .title(parts[0])
                        .author(parts[1])
                        .genre(parts[2])
                        .publisher(parts[3])
                        .build());
    }

    private List<Borrowed> loadBorrowingsFromCSV() {
        return loadFromCSV("src/main/resources/dataSource/borrowed.csv",
                parts -> Borrowed.builder()
                        .borrower(parts[0])
                        .book(parts[1])
                        .borrowedFrom(parseDate(parts[2]))
                        .borrowedTo(parseDate(parts[3]))
                        .build());
    }

    private <T> List<T> loadFromCSV(String fileName, Function<String[], T> mapper) {
        List<T> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            String[] nextLine;
            reader.readNext(); // Skip header line
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length >= 4 && isNotBlankLine(nextLine)) {
                    data.add(mapper.apply(nextLine));
                }
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Error while loading data from {}: {}", fileName, e.getMessage());
        }
        return data;
    }

    private boolean isNotBlankLine(String[] line) {
        return Arrays.stream(line).anyMatch(part -> !part.isEmpty());
    }

    private LocalDate parseDate(String date) {
        return date.isEmpty() ? null : LocalDate.parse(date, dateFormatter);
    }

    private Optional<Character> parseGender(String gender) {
        return gender.length() == 1 ? Optional.of(gender.charAt(0)) : Optional.empty();
    }
}
