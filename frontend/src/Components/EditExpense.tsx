import {FormEvent, useEffect, useState} from "react";
import {Expense} from "./model";
import {useNavigate} from "react-router-dom";

interface EditExpenseProps {
    onItemCreation: (expenses: Array<Expense>) => void;
}

function EditExpense(props: EditExpenseProps) {

    const nav = useNavigate();

    const [purpose, setPurpose] = useState(localStorage.getItem('purpose') ?? '');
    const [description, setDescription] = useState(localStorage.getItem('description') ?? '');
    const [amount, setAmount] = useState(localStorage.getItem('amount') ?? '');
    const [currency, setCurrency] = useState(localStorage.getItem('currency') ?? '');

    useEffect(() => {
        localStorage.setItem('purpose', purpose);
        localStorage.setItem('description', description);
        localStorage.setItem('amount', amount);
        localStorage.setItem('currency', currency);
        }, [purpose, description, amount, currency]);


    const createExpense = (event: FormEvent<HTMLFormElement>) => {
        fetch(`${process.env.REACT_APP_BASE_URL}/expenses`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                purpose: purpose,
                description: description,
                amount: amount,
                currency: currency
                })
        })
            .then(response => response.json())
            .then((itemsFromBackend: Array<Expense>) => {
                props.onItemCreation(itemsFromBackend);
            });
        localStorage.setItem('purpose', '');
        localStorage.setItem('description', '');
        localStorage.setItem('amount', '');
        localStorage.setItem('currency', '');
        nav('/expenses');
    }

    return (
        <div>
            <form onSubmit={ev => createExpense(ev)}>
                <input type="text" placeholder={'Purpose'} value={purpose} onChange={ev => setPurpose(ev.target.value)} />
                <input type="text" placeholder={'Description'} value={description} onChange={ev => setDescription(ev.target.value)} />
                <input type="text" placeholder={'Amount'} value={amount} onChange={ev => setAmount(ev.target.value)} />
                <input type="text" placeholder={'Currency'} value={currency} onChange={ev => setCurrency(ev.target.value)} />
                <button type="submit">anlegen</button>
            </form>
        </div>
    )
}

export default EditExpense;

