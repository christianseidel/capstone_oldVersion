export interface Expense {
    id: string;
    purpose: string;
    description?: string;
    amount: number;
    currency: string;
    user?: string;
    date?: string
}

export interface ExpenseDTO {
    expenses: Array<Expense>;
    sum: number;
}

export interface UserListDTO {
    users: Array<String>;
}

export enum Currency {
    EUR,
    USD,
    CHF,
    GBP,
    JPY
}

export interface User {
    user: string;
}