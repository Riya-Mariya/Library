package com.library.users.controller;

import com.library.users.converter.StringToLocalDateConverter;
import com.library.users.models.Books;
import com.library.users.models.Parameter;
import com.library.users.models.User;
import com.library.users.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/library")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;
    @Autowired
    private StringToLocalDateConverter stringToLocalDateConverter;

    @GetMapping("/borrowedUsers")
    public List<User> getUsersWithBorrowedBooks() {
        return libraryService.getUsersWithBorrowedBooks();
    }

    @GetMapping("/nonTerminatedUsers")
    public List<User> getNonTerminatedUsersWithNoCurrentBorrowings() {
        return libraryService.getNonTerminatedUsersWithNoCurrentBorrowings();
    }

    @GetMapping("/usersBorrowedOnDate")
    public List<User> getUsersBorrowedOnDate(@RequestParam("borrowedDate") String borrowedDate) {
        LocalDate borrowedDateFrom = stringToLocalDateConverter.convert(borrowedDate);

        return libraryService.getUsersBorrowedOnDate(borrowedDateFrom);
    }

    @GetMapping("/booksBorrowedByUserInDateRange")
    public List<Books> getBooksBorrowedByUserInDateRange(@RequestBody Parameter parameters) {
        return libraryService.getBooksBorrowedByUserInDateRange(parameters);
    }

    @GetMapping("/availableBooks")
    public List<Books> getAvailableBooks() {
        return libraryService.getAvailableBooks();
    }
}
