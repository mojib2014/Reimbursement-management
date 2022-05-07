//package com.revature.service;
//
//import com.revature.dao.DaoFactory;
//
//import java.awt.print.Book;
//import java.util.List;
//
//// This class contains the logic of our book application (not DAO, not Servlet)
//// It is the step between the servlet and the DAO
//public class BookService2 {
//
//    public static List<Book> getAllBooks() {
//        BookDao bookDao = DaoFactory.getBookDao();
//        return bookDao.getAllBooks();
//    }
//
//    public static Book getBookById(int id) {
//        BookDao bookDao = DaoFactory.getBookDao();
//        return bookDao.getBookById(id);
//    }
//
//    public static Book add(Book book) {
//        BookDao bookDao = DaoFactory.getBookDao();
//        bookDao.insert(book);
//        return book;
//    }
//
//    public static Book update(Book book) {
//        BookDao bookDao = DaoFactory.getBookDao();
//        // update the book:
//        bookDao.update(book);
//        return book;
//    }
//
//    public static boolean delete(int id) {
//        BookDao bookDao = DaoFactory.getBookDao();
//        // update the book, return whether deletion was successful:
//        return bookDao.delete(id);
//    }
//}
