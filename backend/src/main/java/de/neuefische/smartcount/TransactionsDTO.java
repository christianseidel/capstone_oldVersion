package de.neuefische.smartcount;

import lombok.AllArgsConstructor;
import lombok.Data;

//  This Data Transfer Object collects and transfers a list of those transactions,
//  which will be needed to settle all debts within the group of users of the Cashbook.

@Data
@AllArgsConstructor
public class TransactionsDTO {

    private String userFrom;
    private String userTo;
    private double balance;

}
