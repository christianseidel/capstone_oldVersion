import {Expense, ExpenseDTO} from "./model";
import {useNavigate} from "react-router-dom";
import './expenses.css'
import icon_edit from "../Media/Images/pen.png"
import React, {useState} from "react";
import {useAuth} from "./UserManagement/AuthProvider";


interface ExpenseItemProps {
    expense: Expense
    onExpenseChange: (expenseDTO: ExpenseDTO) => void;
    onItemDeletion: () => void;
}

function ExpenseItem(props: ExpenseItemProps) {

    const nav = useNavigate();
    const {token} = useAuth();

    function deleteItem() {
        fetch(`${process.env.REACT_APP_BASE_URL}/api/expenses/${props.expense.id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(() => props.onItemDeletion());
    }

    const [mouseOverButton, setMouseOverButton] = useState('item');

    return (
        <div className={"list_OfAllItems"}>
          <div className={mouseOverButton==='item' ? 'item-wrapper_regular' : 'item-wrapper_small'}>
              <div className={mouseOverButton}>
                <div className={"item_firstLine"}>
                    <span> {props.expense.purpose} </span>
                    <span> {(props.expense.amount).toLocaleString('de-De', {
                        style: 'currency',
                        currency: props.expense.currency,
                        minimumFractionDigits: 2
                    })} </span>
                </div>
                <div className={"item_secondLine"}>
                    <div className={"item_secondLine_text"}>
                        <span> {props.expense.description} </span>
                        {props.expense.user && <div> <span id={"user_color"}>&#9679;</span> {props.expense.user}</div>}
                    </div>
                    <div className={"item_secondLine_buttons"}>
                        <span>
                            <button id={'editItem-button_FrontPage'} type="submit"
                                    onClick={() => nav(`/edit/${props.expense.id}`)}
                                    onMouseOver={()=>setMouseOverButton('item-MouseOverEdit')}
                                    onMouseOut={()=>setMouseOverButton('item')}
                                    >
                                <img id={'editItem-button_FrontPage-image'} src={icon_edit} alt={'edit item'} />
                            </button>
                        </span>
                        <span>
                            <button id={'deleteItem-button_FrontPage'} type="submit"
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
          </div>
        </div>
    );
}

export default ExpenseItem