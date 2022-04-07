import {FormEvent, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import './expenses.css'

function CreateExpense() {

    const nav = useNavigate();

    const [purpose, setPurpose] = useState(localStorage.getItem('purpose') ?? '');
    const [description, setDescription] = useState(localStorage.getItem('description') ?? '');
    const [amount, setAmount] = useState(localStorage.getItem('amount') ?? '');
    const [currency, setCurrency] = useState(localStorage.getItem('currency') ?? 'EUR');

    useEffect(() => {
        localStorage.setItem('purpose', purpose);
        localStorage.setItem('description', description);
        localStorage.setItem('amount', amount);
        localStorage.setItem('currency', currency);
    }, [purpose, description, amount, currency]);


    function clearForm() {
        localStorage.setItem('purpose', '');
        localStorage.setItem('description', '');
        localStorage.setItem('amount', '');
    }

    const postExpense = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
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
            .then(() => clearForm())
            .then(() => nav('/expenses'));
    }

    function cancelEdit() {
        clearForm();
        nav('/expenses');
    }

    return (
        <div>
            <h2>Ausgabe hinzufügen</h2>
            <form onSubmit={ev => postExpense(ev)}>
                <input type="text" placeholder={'Bezeichnung'} value={purpose} required
                       onChange={ev => setPurpose(ev.target.value)}/>
                <input type="text" placeholder={'Beschreibung'} value={description}
                       onChange={ev => setDescription(ev.target.value)}/>
                <input type="text" placeholder={'Betrag'} value={amount} required
                       onChange={ev => setAmount(ev.target.value)}/>

                <select value={currency}
                        onChange={ev => setCurrency(ev.target.value)}>
                    <option value={"EUR"}>Euro</option>
                    <option value={"USD"}>US-Dollar</option>
                    <option value={"GBP"}>Britisches Pfund</option>
                    <option value={"CHF"}>Schweizer Franken</option>
                    <option value={"JPY"}>Yen</option>
                </select>
                <button id={"edit-button"} type="submit"> &#10004; anlegen</button>
            </form>
            <button id={"cancel-button"} type="submit" onClick={event => cancelEdit()}> &#10008; abbrechen</button>
        </div>
    )
}

export default CreateExpense;

