export interface Expense {
    id: string;
    purpose: string;
    description?: string;
    amount: number;
    currency: string;
    user?: string;
    date?: string
}

export enum Currency {
    EUR,
    USD,
    CHF,
    GBP,
    JPY
}