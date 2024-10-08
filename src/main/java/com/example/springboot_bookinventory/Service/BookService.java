package com.example.springboot_bookinventory.Service;

import com.example.springboot_bookinventory.Model.Book;
import com.example.springboot_bookinventory.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }
    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }
}
