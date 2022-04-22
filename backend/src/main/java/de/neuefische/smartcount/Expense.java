package de.neuefische.smartcount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document (collection = "expenses")
@Data
@NoArgsConstructor
public class Expense {

    @Id
    private String id;
    private String purpose;
    private String description;
    private double amount;
    private Currency currency;
    private String user;
}
