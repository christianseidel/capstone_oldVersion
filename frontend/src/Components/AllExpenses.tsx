import React, {useState, useEffect} from 'react';
import {ExpenseDTO} from "./model";
import ExpenseItem from "./ExpenseItem"
import {useNavigate} from "react-router-dom";
import './expenses.css'

function AllExpenses() {

    const nav = useNavigate();
    const [expenseDTO, setExpenseDTO] = useState({} as ExpenseDTO);
    const loading : String = 'loading ...'

    useEffect(() => {
        fetchAllExpenses()
    }, []);

    const fetchAllExpenses = () => {
        fetch(`${process.env.REACT_APP_BASE_URL}/expenses`)
            .then(response => response.json())
            .then((responseBody: ExpenseDTO) => {
                setExpenseDTO(responseBody)
            })
    }

    return (
        <div>
            <div>
                <h1>SmartCount &ndash; Your Multi-User Cashbook</h1>
            </div>

            <div>
                {expenseDTO.expenses ? expenseDTO.expenses.map(item => <ExpenseItem key={item.id} expense={item}
                                                                                onItemDeletion={fetchAllExpenses}
                                                                                onExpenseChange={setExpenseDTO}/>)
                    : <span>{loading}</span>}
            </div>
            <div className={"sum"}><span>Gesamtausgaben:</span><span>{expenseDTO.sum ? (expenseDTO.sum).toLocaleString('de-De', {
                style: 'currency', currency: 'EUR', minimumFractionDigits: 2  // hard-coded "EUR" will be solved and implemented at a later point in time
            })
                : <span>{loading}</span>}</span></div>

            <div>
                <button id={"create-button_FrontPage"} onClick={() => nav('/edit')}>Neue Ausgabe hinzufügen</button>
            </div>
        </div>
    );
}

export default AllExpenses;
