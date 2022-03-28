package de.neuefische.smartcount;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpensesRepository extends MongoRepository<Expense, String> {
}
