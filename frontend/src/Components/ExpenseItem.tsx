import {Expense, ExpenseDTO} from "./model";
import {useNavigate} from "react-router-dom";
import './expenses.css'
import icon_edit from "../Media/Images/pencil.png"
import React from "react";


interface ExpenseItemProps {
    expense: Expense
    onExpenseChange: (expenseDTO: ExpenseDTO) => void;
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
          <div className={"item"}>
                <div className={"item_firstLine"}>
                    <span> {props.expense.purpose} </span> &nbsp; &nbsp;
                    <span> {(props.expense.amount).toLocaleString('de-De', {
                        style: 'currency',
                        currency: props.expense.currency,
                        minimumFractionDigits: 2
                    })} </span>
                </div>
                <div className={"item_secondLine"}>
                    <span> {props.expense.description} </span>
                    <div className={"item_secondLine_buttons"}>
                        <span>
                            <button id={'edit-button_FrontPage'} type="submit" onClick={() => nav(`/edit/${props.expense.id}`)}>
                            <img height={'16px'} width={'16px'} src={icon_edit} alt={'edit item'}/> </button>
                        </span>
                        <span>
                            <button id={'delete-button_FrontPage'} type="submit" onClick={deleteItem}>&#10006;</button>
                        </span>
                    </div>
                </div>
            </div>
    );
}

export default ExpenseItem