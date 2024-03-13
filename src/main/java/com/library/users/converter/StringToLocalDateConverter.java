package com.library.users.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    private static final List<DateTimeFormatter> DATE_FORMATS = Arrays.asList(
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("MM-dd-yyyy")
    );

    @Override
    public LocalDate convert(String source) {
        for (DateTimeFormatter formatter : DATE_FORMATS) {
            try {
                return LocalDate.parse(source, formatter);
            } catch (Exception ignored) {
            }
        }
        throw new IllegalArgumentException("Invalid date format: " + source);
    }
}
