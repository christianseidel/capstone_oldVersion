export interface Expense {
    id?: string;
    purpose: string;
    description?: string;
    amount: number;
    currency: string;
    user?: string;
    date?: string
}

export enum Currency {
    Euro = 'Euro',
    USDollar = 'US-Dollar',
    SwissFranc = "Swiss Franc",
    BritishPound = 'British Pound'
}