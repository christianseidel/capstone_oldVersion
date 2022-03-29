import React, {useState, useEffect} from 'react';
import {Expense} from "./model";
import ExpenseItem from "./ExpenseItem"

function AllExpenses() {

    const [list, setList] = useState([] as Array<Expense>);


    useEffect(() => {
        fetchAllExpenses()
    }, []);

    const fetchAllExpenses = () => {
        fetch(`${process.env.REACT_APP_BASE_URL}/expenses`)
            .then(response => response.json())
            .then((responseBody: Array<Expense>) => {
                setList(responseBody)
            })
    }


    return (
        <div>
            <div>
                <h1>SmartCount &ndash; Your Multi-User Cashbook</h1>
            </div>
            <div>
                {list.map(item => <ExpenseItem key={item.id} expense={item}
                                               onExpenseChange={setList} />)}
            </div>
        </div>
    );
}

export default AllExpenses;
