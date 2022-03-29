import {Expense} from "./model";

interface ExpenseItemProps {
    expense: Expense
    onExpenseChange: (list: Array<Expense>) => void;
    onItemDeletion: () => void;
}

function ExpenseItem(props: ExpenseItemProps) {

    function deleteItem() {
        fetch(`${process.env.REACT_APP_BASE_URL}/expenses/${props.expense.id}`, {
            method: 'DELETE'
        })
        .then(() => props.onItemDeletion());
    }

    return (
        <div>
            <div>
                <span> {props.expense.purpose} </span> <span><button id={'delete-button'} type="submit" onClick={deleteItem}>&#10006;</button></span>
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