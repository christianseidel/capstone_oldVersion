import {Expense, ExpenseDTO} from "./model";
import {useNavigate} from "react-router-dom";

interface ExpenseItemProps {
    expense: Expense
    onExpenseChange: ({} : ExpenseDTO) => void;
    onItemDeletion: () => void;
}

function ExpenseItem(props: ExpenseItemProps) {

    const nav = useNavigate();

    function deleteItem() {
        fetch(`${process.env.REACT_APP_BASE_URL}/expenses/${props.expense.id}`, {
            method: 'DELETE'
        })
            .then(() => props.onItemDeletion());
    }


    return (
        <div>
            <div>
                <span> {props.expense.purpose} </span> &nbsp; &nbsp;
                <span><button id={'delete-button'} type="submit" onClick={() => nav (`/edit/${props.expense.id}`)}>&#9998;</button></span> &nbsp;
                <span><button id={'delete-button'} type="submit" onClick={deleteItem}>&#10006;</button></span>
            </div>
            <div>
                <span> {props.expense.description} </span>
            </div>
            <div>
                <span> {(props.expense.amount).toFixed(2)} &nbsp;{props.expense.currency}</span>
                <div> {(props.expense.amount).toLocaleString('de-De', {style: 'currency', currency: props.expense.currency, minimumFractionDigits: 2})} </div>
            </div>
            <br/>

        </div>
    );
}

export default ExpenseItem