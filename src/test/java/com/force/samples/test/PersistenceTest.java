package com.force.samples.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.force.samples.entity.Author;
import com.force.samples.entity.Book;


public class PersistenceTest {

	private static Logger log = null;
	
	@BeforeClass
	public static void testClassSetup() {
		log = LoggerFactory.getLogger(PersistenceTest.class);
		log.warn("Be sure to create the schema before running these tests!");
        cleanTable("Book");
        cleanTable("Author");
	}

	@Test
	public void testDatabaseSaveAndRetrieve() {
		log.info("Running testDatabaseSaveAndRetrieve");
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("exampleHibernateJPA");
		EntityManager em = emf.createEntityManager();
		
		log.info("Creating and persisting entity...");
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		Author author = new Author();
		author.setFirstName("JRR");
		author.setLastName("Tolkein");

		Book firstBook = newBook(author, "Fellowship of the Ring");
        Book secondBook = newBook(author, "The Hobbit");

        em.persist(firstBook);
        em.persist(secondBook);

        author.setBooks(listOf(firstBook, secondBook));
        em.persist(author);
		
		tx.commit();
		
		// Now read it back and verify
		log.info("Reading back from database and verifying...");
		EntityTransaction readTx = em.getTransaction();
		readTx.begin();
//        author = em.find(Author.class, author.getId());
		firstBook.setTitle("Not the Hobbit");
        em.merge(author);
		readTx.commit();

        Book notTheHobbit = em.find(Book.class, firstBook.getId());
        Assert.assertEquals("Not the Hobbit", notTheHobbit.getTitle());
	}

    private Book newBook(Author author, String title) {
        Book book = new Book();
        book.setAuthor(author);
        book.setTitle(title);
        book.setPublicationDate(new GregorianCalendar(2011, 11, 15).getTime());
        return book;
    }

    private List<Book> listOf(Book... books) {
        List<Book> bookList = new ArrayList<Book>();
        for (Book b : books)
            bookList.add(b);
        return bookList;
    }

    private static void cleanTable(String tableName) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("exampleHibernateJPA");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("Delete From " + tableName).executeUpdate();
        em.getTransaction().commit();
        Long countRow = (Long)em.createQuery("Select count(x) From " + tableName + " x").getSingleResult();
        Assert.assertEquals((Long)0L, countRow);
    }
}
