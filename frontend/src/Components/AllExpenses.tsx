import React, {useState, useEffect} from 'react';
import {Expense, ExpenseDTO} from "./model";
import ExpenseItem from "./ExpenseItem"
import {useNavigate} from "react-router-dom";

function AllExpenses() {

    const nav = useNavigate();
    const [response, setResponse] = useState({} as ExpenseDTO);

    useEffect(() => {
        fetchAllExpenses()
    }, []);

    const fetchAllExpenses = () => {
        fetch(`${process.env.REACT_APP_BASE_URL}/expenses`)
            .then(response => response.json())
            .then((responseBody: ExpenseDTO) => {
                setResponse(responseBody)
            })
    }


       return (
        <div>
            <div>
                <h1>SmartCount &ndash; Your Multi-User Cashbook</h1>
            </div>

            <div>
                {response.expenses ? response.expenses.map(item => <ExpenseItem key={item.id} expense={item}
                                               onItemDeletion={fetchAllExpenses}
                                               onExpenseChange={setResponse} />)
                    : <span>"loading ..."</span>}
            </div>
            <div>{response.sum}</div>
            <div>
                <button onClick={() => nav ('/edit')}>Neue Ausgabe hinzufügen</button>
            </div>
        </div>
    );
}

export default AllExpenses;
