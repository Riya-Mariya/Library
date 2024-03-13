package com.library.users.service.impl;

import com.library.users.converter.StringToLocalDateConverter;
import com.library.users.loader.DataLoader;
import com.library.users.models.*;
import com.library.users.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryServiceImpl implements LibraryService {

    private final DataSource dataSource;
    @Autowired
    private StringToLocalDateConverter stringToLocalDateConverter;

    @Autowired
    public LibraryServiceImpl(DataLoader dataLoader) {
        this.dataSource = dataLoader.getDataSource();
    }

    @Override
    public List<User> getUsersWithBorrowedBooks() {
        return dataSource.getUsersDetails().stream()
                .filter(user -> dataSource.getBorrowingsDetails().stream()
                        .anyMatch(borrowed -> borrowed.getBorrower().equals(user.getName()+","+user.getFirstName())))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getNonTerminatedUsersWithNoCurrentBorrowings() {
        return dataSource.getUsersDetails().stream()
                .filter(user -> dataSource.getBorrowingsDetails().stream()
                        .noneMatch(borrowed -> borrowed.getBorrower().equals(user.getName()+","+user.getFirstName())))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getUsersBorrowedOnDate(LocalDate date) {
        return dataSource.getUsersDetails().stream()
                .filter(user -> dataSource.getBorrowingsDetails().stream()
                        .anyMatch(borrowed -> borrowed.getBorrowedFrom().isEqual(date)
                                && borrowed.getBorrower().equals(user.getName()+","+user.getFirstName())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Books> getBooksBorrowedByUserInDateRange(Parameter parameters) {
        String user = parameters.getUserName().replaceAll(" ",",");
        LocalDate startDate = stringToLocalDateConverter.convert(parameters.getStartDate());
        LocalDate endDate = stringToLocalDateConverter.convert(parameters.getEndDate());

        List<String> borrowedBooks = dataSource.getBorrowingsDetails().stream()
                .filter(borrowed -> borrowed.getBorrower().equals(user)
                        && (startDate == null || borrowed.getBorrowedFrom().isEqual(startDate) || borrowed.getBorrowedFrom().isAfter(startDate))
                        && (endDate == null || borrowed.getBorrowedTo().isEqual(endDate) || borrowed.getBorrowedTo().isBefore(endDate)))
                .map(Borrowed::getBook)
                .toList();

        return dataSource.getBooksDetails().stream()
                .filter(book->borrowedBooks.contains(book.getTitle()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Books> getAvailableBooks() {
        List<String> borrowedBooks = dataSource.getBorrowingsDetails().stream()
                .map(Borrowed::getBook)
                .toList();
        return dataSource.getBooksDetails().stream()
                .filter(book -> !borrowedBooks.contains(book.getTitle()))
                .collect(Collectors.toList());
    }


}
