import {Expense, ExpenseDTO} from "./model";
import {useNavigate} from "react-router-dom";
import './expenses.css'
import icon_edit from "../Media/Images/pen.png"
import React, {useEffect, useState} from "react";


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

    const [mouseOverButton, setMouseOverButton] = useState('item');
/*
        const isMouseOverButton = (MouseOver : String) => {
        if (MouseOver==="Edit") {MouseOverButton = "item-MouseOverEdit"} else
        if (MouseOver==="Delete") {MouseOverButton = "item-MouseOverDelete"} else {
        MouseOverButton = "item";}
    };
*/
    return (
          <div className={mouseOverButton}>
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
                            <button id={'edit-button_FrontPage'} type="submit"
                                    onClick={() => nav(`/edit/${props.expense.id}`)}
                                    onMouseOver={()=>setMouseOverButton('item-MouseOverEdit')}
                                    onMouseOut={()=>setMouseOverButton('item')}
                                    >
                                <img id={'edit-button_FrontPage-image'} src={icon_edit} alt={'edit item'} />
                            </button>
                        </span>
                        <span>
                            <button id={'delete-button_FrontPage'} type="submit"
                                    onClick={deleteItem}
                                    onMouseOver={()=>setMouseOverButton('item-MouseOverDelete')}
                                    onMouseOut={()=>setMouseOverButton('item')}
                                    >
                                &#10006;
                            </button>
                        </span>
                    </div>
                </div>
            </div>
    );
}

export default ExpenseItem