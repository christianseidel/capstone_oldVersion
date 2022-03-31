package de.neuefische.smartcount;

import lombok.Data;
import lombok.NoArgsConstructor;
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
    private Double amount;
    private Currency currency;
    private String user;
    private Date date;
}
