//package com.revature.servlets;
//
//import com.revature.dao.DaoFactory;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import java.awt.print.Book;
//import java.io.*;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//
//public class TestBookServlet {
//
//    private static BookDao bookDao;
//
//    @BeforeEach
//    public void setUp() {
//        // since we're testing with h2, create the tables every time:
//        bookDao = DaoFactory.getBookDao();
//        bookDao.initTables();
//        bookDao.fillTables();
//    }
//
//    @Test
//    public void testGetAllBooks() throws IOException {
//        // mock the request/response:
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        // set up the print writer:
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter writer = new PrintWriter(stringWriter);
//        // return the writer to mimic the response's writer:
//        when(response.getWriter()).thenReturn(writer);
//
//        // create a new book servlet and do the get method:
//        new BookServlet().doGet(request, response);
//
//        // flush the writer, make sure all the output is written:
//        writer.flush();
//        // assert that the result contains all of the proper books:
//        assertTrue(stringWriter.toString().contains("Book{id=1, name='book 1', author='author 1', description='description 1', year=1}"));
//        assertTrue(stringWriter.toString().contains("Book{id=2, name='book 2', author='author 2', description='description 2', year=2}"));
//        assertTrue(stringWriter.toString().contains("Book{id=3, name='book 3', author='author 3', description='description 3', year=3}"));
//    }
//
//    @Test
//    public void TestGetSpecificBook() throws IOException {
//        // mock the request/response:
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        // configure/mocking the id query parameter (id = 1):
//        when(request.getParameter("id")).thenReturn("1");
//
//        // set up the print writer:
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter writer = new PrintWriter(stringWriter);
//        when(response.getWriter()).thenReturn(writer);
//
//        // create a new book servlet and do the get method:
//        new BookServlet().doGet(request, response);
//
//        // verify the parameter:
//        verify(request, atLeast(1)).getParameter("id");
//        // flush the writer, make sure all the output is written:
//        writer.flush();
//        // assert that the result contains the proper book:
//        assertTrue(stringWriter.toString().contains("Book{id=1, name='book 1', author='author 1', description='description 1', year=1}"));
//    }
//
//    @Test
//    public void TestPostBook() throws IOException {
//        // mock the request/response:
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        // since we take in a buffered reader to read the body, we simulate it by putting
//        // mock data in a local file called book.txt, this is simulating what we would put in body of the request
//        FileReader fr = new FileReader("src/test/java/com/revature/servlets/book.txt");
//        BufferedReader t = new BufferedReader(fr);
//        // configure the buffered reader:
//        when(request.getReader()).thenReturn(t);
//
//        // set up the print writer:
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter writer = new PrintWriter(stringWriter);
//        when(response.getWriter()).thenReturn(writer);
//
//        // create a new book servlet and do the post method:
//        new BookServlet().doPost(request, response);
//
//        // flush the writer, make sure all the output is written:
//        writer.flush();
//        // assert that the result contains the proper book:
//        assertTrue(stringWriter.toString().contains("Book{id=4, name='new book', author='new author', description='new description', year=2000}"));
//        // assert that the book was inserted properly:
//        Book book = bookDao.getBookById(4);
//        assertEquals(book.getName(), "new book");
//        assertEquals(book.getDescription(), "new description");
//        assertEquals(book.getAuthor(), "new author");
//        assertEquals(book.getYear(), 2000);
//    }
//
//    @Test
//    public void TestPutBook() throws IOException {
//        // mock the request/response:
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        // since we take in a buffered reader to read the body, we simulate it by putting
//        // mock data in a local file called book_with_id.txt
//        FileReader fr = new FileReader("src/test/java/com/revature/servlets/book_with_id.txt");
//        BufferedReader t = new BufferedReader(fr);
//        // configure the request to use the buffered reader:
//        when(request.getReader()).thenReturn(t);
//
//        // set up the print writer:
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter writer = new PrintWriter(stringWriter);
//        when(response.getWriter()).thenReturn(writer);
//
//        // create a new book servlet and do the post method:
//        new BookServlet().doPut(request, response);
//
//        // flush the writer, make sure all the output is written:
//        writer.flush();
//        // assert that the result contains the proper book:
//        assertTrue(stringWriter.toString().contains("Book{id=3, name='new book', author='new author', description='new description', year=2000}"));
//        // assert that the book was updated properly:
//        Book book = bookDao.getBookById(3);
//        assertEquals(book.getName(), "new book");
//        assertEquals(book.getDescription(), "new description");
//        assertEquals(book.getAuthor(), "new author");
//        assertEquals(book.getYear(), 2000);
//    }
//
//    @Test
//    public void TestDeleteBook() throws IOException {
//        // mock the request/response:
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        // configure the id parameter:
//        when(request.getParameter("id")).thenReturn("3");
//
//        // set up the print writer:
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter writer = new PrintWriter(stringWriter);
//        when(response.getWriter()).thenReturn(writer);
//
//        // create a new book servlet and do the get method:
//        new BookServlet().doDelete(request, response);
//
//        // verify the parameter:
//        verify(request, atLeast(1)).getParameter("id");
//        // flush the writer, make sure all the output is written:
//        writer.flush();
//        assertEquals("Deletion success!", stringWriter.toString());
//        // make sure book is deleted:
//        assertNull(bookDao.getBookById(3));
//    }
//
//    @Test
//    public void TestDeleteBookNotThere() throws IOException {
//        // mock the request/response:
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//
//        // configure the id parameter:
//        when(request.getParameter("id")).thenReturn("4");
//
//        // set up the print writer:
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter writer = new PrintWriter(stringWriter);
//        when(response.getWriter()).thenReturn(writer);
//
//        // create a new book servlet and do the get method:
//        new BookServlet().doDelete(request, response);
//
//        // verify the parameter:
//        verify(request, atLeast(1)).getParameter("id");
//        // flush the writer, make sure all the output is written:
//        writer.flush();
//        assertEquals("Deletion failed!", stringWriter.toString());
//    }
//
//
//}
