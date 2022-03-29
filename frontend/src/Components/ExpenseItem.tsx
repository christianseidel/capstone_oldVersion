import {Expense, Currency} from "./model";
import exp from "constants";

interface ExpenseItemProps {
    expense: Expense
    onExpenseChange: (list: Array<Expense>) => void;
}

function ExpenseItem(props: ExpenseItemProps) {

    function deleteItem() {}  // next task in-line

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