import React, {useState, useEffect} from 'react';
import {ExpenseDTO} from "./model";
import ExpenseItem from "./ExpenseItem"
import {useNavigate} from "react-router-dom";
import './expenses.css'

function AllExpenses() {

    const nav = useNavigate();
    const [response, setResponse] = useState({} as ExpenseDTO);
    const loading : String = 'loading ...'

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
                                                                                onExpenseChange={setResponse}/>)
                    : <span>{loading}</span>}
            </div>
            <div className={"sum"}><span>Gesamtausgaben:</span><span>{response.sum ? (response.sum).toLocaleString('de-De', {
                style: 'currency', currency: 'EUR', minimumFractionDigits: 2
            })
                : <span>{loading}</span>}</span></div>

            <div>
                <button id={"create-button_FrontPage"} onClick={() => nav('/edit')}>Neue Ausgabe hinzufügen</button>
            </div>
        </div>
    );
}

export default AllExpenses;
