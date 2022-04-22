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

export interface TransactionsDTO {
    userFrom: string;
    userTo: string;
    balance: number;
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

export const checkLogin = (status: number) => {
    if((status === 401)||(status === 403)) {
        throw new Error("forbidden")}
}
