package hello;

/**
 * @author Yasin Zhang
 */
public interface BookRepository {

    Book getByIsbn(String isbn);

}
