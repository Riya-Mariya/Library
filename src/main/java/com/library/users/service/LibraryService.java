package com.library.users.service;

import com.library.users.models.Books;
import com.library.users.models.Parameter;
import com.library.users.models.User;

import java.time.LocalDate;
import java.util.List;

public interface LibraryService {
    List<User> getUsersWithBorrowedBooks();

    List<User> getNonTerminatedUsersWithNoCurrentBorrowings();

    List<User> getUsersBorrowedOnDate(LocalDate date);

    List<Books> getBooksBorrowedByUserInDateRange(Parameter parameters);

    List<Books> getAvailableBooks();
}
