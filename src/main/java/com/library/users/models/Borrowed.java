package com.library.users.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Borrowed {
    private String borrower;
    private String book;
    private LocalDate borrowedFrom;
    private LocalDate borrowedTo;
}
