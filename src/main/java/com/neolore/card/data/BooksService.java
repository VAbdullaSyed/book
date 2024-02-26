package com.neolore.card.data;

import com.neolore.card.config.RoutingDataSource;
import com.neolore.card.config.WithLoadBalancedReaderDataSource;
import com.neolore.card.data.Book;
import com.neolore.card.data.BooksRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class BooksService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BooksService.class);
    private final BooksRepository booksRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Book save(Book zilchTxn) {
        LOGGER.info("Current node: {}", this.booksRepository.getNodeId());
        return booksRepository.save(zilchTxn);
    }

    @WithLoadBalancedReaderDataSource
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<Book> finalAll() {
        LOGGER.info("Current node: {}", this.booksRepository.getNodeId());
        return booksRepository.findAll();
    }
}
