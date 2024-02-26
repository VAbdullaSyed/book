package com.neolore.card.service;

import com.neolore.card.data.BooksRepository;
import com.neolore.card.data.Book;
import com.neolore.card.data.BooksService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    BooksService booksService;

    @Mock
    BooksRepository booksRepository;


    @Test
    public void testFindAll_returnsAllTxns(){
        List<Book> list = new ArrayList<>();
        Book zilchTxn1 = new Book(1l, "tesco", new BigDecimal("11.22"), 14l);
        Book zilchTxn2 = new Book(2l, "pharmacy", new BigDecimal("5.50"), 14l);
        list.add(zilchTxn1);
        list.add(zilchTxn2);

        when(booksRepository.findAll()).thenReturn(list);
        List<Book> zilchTxns = booksService.finalAll();
        assertEquals(2, list.size(), "More than two record saved");
        verify(booksRepository, times(1)).findAll();

    }

    @Test
    public void testSave_CallsRepositoryOnce(){
        Book zilchTxn1 = new Book(1l, "tesco", new BigDecimal("11.22"), 14l);
        booksService.save(zilchTxn1);
        verify(booksRepository, times(1)).save(zilchTxn1);
    }

}