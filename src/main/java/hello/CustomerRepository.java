package hello;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Yasin Zhang
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);
}
