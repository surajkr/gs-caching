import com.example.caching.Book;
import com.example.caching.BookRepository;
import com.example.caching.SimpleBookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

@SpringBootTest(classes = {CachingApplicationTest.class,
        SimpleBookRepository.class})
@ContextConfiguration(classes = {TestCacheConfiguration.class})
public class CachingApplicationTest {

    @Autowired
    private BookRepository repository;



    @Autowired
    private CacheManager manager;

    @Test
    public void testContextLoads()
    {

    }


    @Test
    public void testCacheHit()
    {
        Book book1 = repository.getByIsbn("1234");

        Book cached = repository.getByIsbn("1234");
        Assertions.assertSame(book1, cached);

        Book book2=repository.getByIsbn("1235");
        assertNotSame(book1,book2);


    }
    @Test
    public void testCacheEvict()
    {
        Book book1 = repository.getByIsbn("1234");

        manager.getCache("books").evict("1234");
        Book book1Refetch = repository.getByIsbn("1234");
        assertNotSame(book1,book1Refetch);

    }

    @Test
    public void testCacheMany()
    {
        Book book1 = repository.getByIsbn("1234");
        Book book2 = repository.getByIsbn("1235");
        Book book3 = repository.getByIsbn("1236");

        final Cache books = manager.getCache("books");
        assertNotNull(books.get("1236"));


    }

}
