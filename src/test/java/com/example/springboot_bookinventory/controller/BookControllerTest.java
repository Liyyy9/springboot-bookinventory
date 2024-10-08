package com.example.springboot_bookinventory.controller;

import com.example.springboot_bookinventory.Model.Book;
import com.example.springboot_bookinventory.Repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_ENDPOINT = "/api/books";

    private Book book1, book2;
    private List<Book> bookList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();

        book1 = Book.builder()
                .title("Book Title 1")
                .author("Author 1")
                .build();

        book2 = Book.builder()
                .title("Book Title 2")
                .author("Author 2")
                .build();

        bookList.add(book1);
        bookList.add(book2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllBooks() throws Exception{
        bookRepository.saveAll(bookList);

        ResultActions resultActions = mockMvc.perform(get(API_ENDPOINT));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(bookList.size())));
    }

    @Test
    void getBookById() throws Exception{
        bookRepository.save(book1);

        ResultActions resultActions = mockMvc.perform(get(API_ENDPOINT.concat("/{id}"), book1.getId()));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title").value(book1.getTitle()))
                .andExpect(jsonPath("$.author").value(book1.getAuthor()));
    }

    @Test
    void createBook() throws Exception{
        String requestBody = objectMapper.writeValueAsString(book1);

        ResultActions resultActions = mockMvc.perform(post(API_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.title").value(book1.getTitle()))
                .andExpect(jsonPath("$.author").value(book1.getAuthor()))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()));
    }

    @Test
    void updateBook() throws Exception{

        bookRepository.save(book2);
        Book updateBook1 = bookRepository.findById(book2.getId()).get();
        updateBook1.setTitle("Book Title 2_1");
        updateBook1.setAuthor("Author 2_1");

        String requestBody = objectMapper.writeValueAsString(updateBook1);

        ResultActions resultActions = mockMvc.perform(put(API_ENDPOINT.concat("/{id}"), updateBook1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title").value(updateBook1.getTitle()))
                .andExpect(jsonPath("$.author").value(updateBook1.getAuthor()));
    }

    @Test
    void deleteBook() throws Exception{
        bookRepository.save(book1);
        Book deleteBook1 = bookRepository.findById(book1.getId()).get();
        String expectedResponse = String.format("%s deleted successfully", deleteBook1.getTitle());

        ResultActions resultActions = mockMvc.perform(delete(API_ENDPOINT.concat("/{id}"), deleteBook1.getId())
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(result -> assertEquals(expectedResponse, result.getResponse().getContentAsString()));
    }
}