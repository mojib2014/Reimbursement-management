//package com.revature.service;
//
//import com.revature.dao.DaoFactory;
//
//import java.awt.print.Book;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Scanner;
//
///*
//a service class is where we store methods that perform the logical operations
//In our servlets example, we can disregard this class because it is responsible for taking in information via the command line.
//The Services that are called for the servlet example are in the BookService2 class.
// */
//
//public class BookService {
//
//    // take in user input and insert a book into the database:
//    public static void insertBook() {
//        Scanner scanner = new Scanner(System.in);
//        // notice that we don't ask for an id (this will be auto-generated)
//        System.out.println("Please enter the name of the book: ");
//        String name = scanner.nextLine();
//        System.out.println("Please enter the author of the book: ");
//        String author = scanner.nextLine();
//        System.out.println("Please enter the description of the book: ");
//        String description = scanner.nextLine();
//        System.out.println("Please enter what year the book was made: ");
//        int year = scanner.nextInt();
//        Book book = new Book(name, author, description, year);
//        BookDao bookDao = DaoFactory.getBookDao();
//        bookDao.insertStoredProcedure(book);
//    }
//
//    public static void getBookById() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Please enter the id of the book: ");
//        int id = scanner.nextInt();
//        BookDao bookDao = DaoFactory.getBookDao();
//        Book book = bookDao.getBookById(id);
//        System.out.println("Here is the book: " + book.toString());
//    }
//
//    public static void getAllBooks() {
//        System.out.println("All books:");
//        BookDao bookDao = DaoFactory.getBookDao();
//        List<Book> books = bookDao.getAllBooks();
//        for(Book book: books) {
//            System.out.println(book);
//        }
//    }
//
//    public static void updateBook() {
//        // use different scanner for ints and strings
//        Scanner scanner = new Scanner(System.in);
//        Scanner scannerInt = new Scanner(System.in);
//        System.out.println("What is the id of the book you would like to update: ");
//        int id = scannerInt.nextInt();
//        System.out.println("Please enter the new name of the book: ");
//        String name = scanner.nextLine();
//        System.out.println("Please enter the new author of the book: ");
//        String author = scanner.nextLine();
//        System.out.println("Please enter the new description of the book: ");
//        String description = scanner.nextLine();
//        System.out.println("Please enter the new year the book was made: ");
//        int year = scannerInt.nextInt();
//        Book book = new Book(id, name, author, description, year);
//        // we could do this, but the dao factory ensures that only one instance of this dao is ever created, so we don't waste space
//        //BookDao bookDao = new BookDaoImpl();
//        BookDao bookDao = DaoFactory.getBookDao();
//        // update the book:
//        bookDao.update(book);
//    }
//
//    public static void deleteBook() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Please enter the id of the book to be deleted: ");
//        int id = scanner.nextInt();
//        BookDao bookDao = DaoFactory.getBookDao();
//        bookDao.delete(id);
//    }
//
//    public static void insertTicket() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter ticket name: ");
//        String name = scanner.nextLine();
//        LocalDateTime now = LocalDateTime.now();
//        Timestamp timestamp = Timestamp.valueOf(now);
////        Ticket ticket = new Ticket(name, timestamp);
//        BookDao bookDao = DaoFactory.getBookDao();
////        bookDao.insertTicket(ticket);
//
//    }
//
//    public static void main(String[] args) {
//        BookService.getAllBooks();
//    }
//}
