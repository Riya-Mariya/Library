package com.library.users.service.impl;

import com.library.users.converter.StringToLocalDateConverter;
import com.library.users.loader.DataLoader;
import com.library.users.models.Books;
import com.library.users.models.DataSource;
import com.library.users.models.Parameter;
import com.library.users.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LibraryServiceImplTest {

    @Mock
    private DataLoader dataLoader;

    @Mock
    private StringToLocalDateConverter stringToLocalDateConverter;

    private LibraryServiceImpl libraryService;
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        stringToLocalDateConverter = new StringToLocalDateConverter();
        dataLoader = new DataLoader();
        dataSource = dataLoader.getDataSource();
        libraryService = new LibraryServiceImpl(dataLoader);
        ReflectionTestUtils.setField(libraryService, "stringToLocalDateConverter", stringToLocalDateConverter);

    }

    @Test
    void getUsersWithBorrowedBooks() {
        List<User> usersWithBorrowedBooks = libraryService.getUsersWithBorrowedBooks();
        assertEquals(11, usersWithBorrowedBooks.size());
    }

    @Test
    void getNonTerminatedUsersWithNoCurrentBorrowings() {
        User user = User.builder().firstName("test").name("user").gender('m').build();
        dataSource.getUsersDetails().add(user);
        List<User> nonTerminatedUsersWithNoCurrentBorrowings = libraryService.getNonTerminatedUsersWithNoCurrentBorrowings();
        assertEquals(1, nonTerminatedUsersWithNoCurrentBorrowings.size());
    }

    @Test
    void getUsersBorrowedOnDateTest() {
        LocalDate startDate = stringToLocalDateConverter.convert("08-10-2019");

        List<User> usersBorrowedOnDate = libraryService.getUsersBorrowedOnDate(startDate);
        assertEquals(1, usersBorrowedOnDate.size());
    }

    @Test
    void getBooksBorrowedByUserInDateRangeTest() {
        Parameter parameter = new Parameter("Zhungwang Ava", "06/28/2021", "07/28/2021");
        List<Books> booksBorrowedByUserInDateRange = libraryService.getBooksBorrowedByUserInDateRange(parameter);
        assertEquals(1, booksBorrowedByUserInDateRange.size());
    }

    @Test
    void getAvailableBooks() {
        List<Books> availableBooks = libraryService.getAvailableBooks();
        assertEquals(43, availableBooks.size());
    }

    // Add similar tests for other methods
}
