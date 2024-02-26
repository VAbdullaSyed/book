package com.neolore.card.controller;

import com.neolore.card.data.Book;
import com.neolore.card.data.BooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BooksController {
    private final BooksService booksService;

    @PostMapping("save")
    public Book save(@RequestBody Book zilchTxn) {
        return booksService.save(zilchTxn);
    }

    @GetMapping(path = "/list")
    public List<Book> findAll() {
        return booksService.finalAll();
    }
}