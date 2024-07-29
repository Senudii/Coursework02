package service;

import dao.BookDao;
import Entity.Book;
import java.sql.SQLException;
import java.util.List;

public class BookService {
    private BookDao bookDao;

    public BookService(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public List<Book> getAllBooks() throws SQLException {
        return bookDao.getAllBooks();
    }

    public void addBook(Book book) throws SQLException {
        bookDao.addBook(book);
    }

    // Other service methods
}
