package com.library.users.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSource {
    private List<User> usersDetails;
    private List<Books> booksDetails;
    private List<Borrowed> borrowingsDetails;
}
