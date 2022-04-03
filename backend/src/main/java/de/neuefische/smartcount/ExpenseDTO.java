package de.neuefische.smartcount;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class ExpenseDTO {

    private Collection<Expense> expenses;
    private double sum;
}
