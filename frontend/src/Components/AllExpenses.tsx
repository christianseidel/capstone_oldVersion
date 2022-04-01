import React, {useState, useEffect} from 'react';
import {ExpenseDTO} from "./model";
import ExpenseItem from "./ExpenseItem"
import {useNavigate} from "react-router-dom";

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

    function locale(x:number) {
    return x;
    }
    console.log(locale(12312312.178))

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
            <div><b>Summe: {response.sum ? (response.sum).toFixed(2) : <span>{loading}</span>} Euro</b></div>
            <br/>
            <div>{response.sum}</div>
            <div>{(Math.round(response.sum * 100) / 100)}</div>

            <div>
                <button onClick={() => nav('/edit')}>Neue Ausgabe hinzufügen</button>
            </div>
        </div>
    );
}

export default AllExpenses;
