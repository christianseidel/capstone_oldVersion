package de.neuefische.smartcount;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ExpensesRepository extends MongoRepository<Expense, String> {

    Collection<Expense> findAllByUser(String user);

}
