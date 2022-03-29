import {Expense} from "./model";

interface ExpenseItemProps {
    expense: Expense
    onExpenseChange: (list: Array<Expense>) => void;
}

function ExpenseItem(props: ExpenseItemProps) {


    return (
        <div>
            <div>
                <span> {props.expense.purpose} </span>
            </div>
            <div>
                <span> {props.expense.description} </span>
            </div>
            <div>
                <span> {props.expense.amount} &nbsp;{props.expense.currency}</span>
            </div>
            <br/>

        </div>
    );
}

export default ExpenseItem