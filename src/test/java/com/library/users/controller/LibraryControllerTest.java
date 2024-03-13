package com.library.users.controller;

import com.library.users.converter.StringToLocalDateConverter;
import com.library.users.models.Books;
import com.library.users.models.Parameter;
import com.library.users.models.User;
import com.library.users.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LibraryController.class)
class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryService libraryService;

    private final StringToLocalDateConverter stringToLocalDateConverter = new StringToLocalDateConverter();

    @Test
    void getUsersWithBorrowedBooksTest() throws Exception {
        LocalDate startDate = stringToLocalDateConverter.convert("08-10-2019");

        LocalDate endDate = stringToLocalDateConverter.convert("08-10-2021");

        List<User> users = Arrays.asList(new User("John", "Doe", startDate, endDate, 'm'), new User("Alice", "Smith", startDate, null, 'm'));

        when(libraryService.getUsersWithBorrowedBooks()).thenReturn(users);

        mockMvc.perform(get("/library/borrowedUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[1].name").value("Alice"));

        verify(libraryService, times(1)).getUsersWithBorrowedBooks();
    }

    @Test
    void getNonTerminatedUsersWithNoCurrentBorrowingsTest() throws Exception {

        List<User> users = Arrays.asList(new User("John", "Doe", null, null, 'm'), new User("Alice", "Smith", null, null, 'm'));

        when(libraryService.getNonTerminatedUsersWithNoCurrentBorrowings()).thenReturn(users);

        mockMvc.perform(get("/library/nonTerminatedUsers"))
                .andExpect(status().isOk());

        verify(libraryService, times(1)).getNonTerminatedUsersWithNoCurrentBorrowings();
    }

    @Test
    void getUsersBorrowedOnDateTest() throws Exception {

        List<User> users = Arrays.asList(new User("John", "Doe", null, null, 'm'), new User("Alice", "Smith", null, null, 'm'));
        LocalDate startDate = stringToLocalDateConverter.convert("05/02/2008");

        when(libraryService.getUsersBorrowedOnDate(startDate)).thenReturn(users);

        mockMvc.perform(get("/library/usersBorrowedOnDate?borrowedDate=05/02/2008"))
                .andExpect(status().isOk());

        verify(libraryService, times(1)).getUsersBorrowedOnDate(startDate);
    }

    @Test
    void getBooksBorrowedByUserInDateRangeTest() throws Exception {

        List<Books> book = List.of(new Books("test", "testBook", "abs", "Abcs"));
        LocalDate startDate = stringToLocalDateConverter.convert("05/02/2008");
        Parameter parameter = new Parameter("abs", "05/02/2008", "05/05/2008");
        when(libraryService.getBooksBorrowedByUserInDateRange(parameter)).thenReturn(book);

        mockMvc.perform(
                        get("/library/booksBorrowedByUserInDateRange")
                                .contentType("application/json")
                                .content("{\"userName\":\"abs\",\"startDate\":\"05/02/2008\",\"endDate\":\"05/05/2008\"}")
                )
                .andExpect(status().isOk());

        verify(libraryService, times(1)).getBooksBorrowedByUserInDateRange(parameter);
    }

    @Test
    void getAvailableBooksTest() throws Exception {

        List<Books> book = List.of(new Books("test", "testBook", "abs", "Abcs"));
        when(libraryService.getAvailableBooks()).thenReturn(book);

        mockMvc.perform(
                        get("/library/availableBooks")
                )
                .andExpect(status().isOk());

        verify(libraryService, times(1)).getAvailableBooks();
    }
}
