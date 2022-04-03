import {FormEvent, useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {Expense} from "./model";
import './expenses.css'

function EditExpense() {

    const nav = useNavigate();

    const [purpose, setPurpose] = useState(localStorage.getItem('purpose') ?? '');
    const [description, setDescription] = useState(localStorage.getItem('description') ?? '');
    const [amount, setAmount] = useState(localStorage.getItem('amount') ?? 0);
    const [currency, setCurrency] = useState(localStorage.getItem('currency') ?? '');
    const [error, setError] = useState('')

    const id = useParams();

    const clearForm = () => {
        localStorage.setItem('purpose', '');
        localStorage.setItem('description', '');
        localStorage.setItem('amount', '');
    }

    useEffect(() => {
        localStorage.setItem('purpose', purpose);
        localStorage.setItem('description', description);
        localStorage.setItem('amount', `${amount}`);
        localStorage.setItem('currency', currency);
    }, [purpose, description, amount, currency]);


    useEffect(() => {
        setError('');

        fetch(`${process.env.REACT_APP_BASE_URL}/expenses/${id.expenseId}`, {
            method: 'GET'
        })
            .then(response => {
                if (response.ok) {
                    return response.json()
                } else {
                    throw Error("Ein Eintrag mit der ID " + id.expenseId + " wurde nicht gefunden.")
                }
            })
            .then((data: Expense) => {
                setPurpose(data.purpose);
                setDescription(data.description ?? '');
                setAmount(data.amount);
                setCurrency(data.currency);
                clearForm();
            })
            .catch(e => setError(e.message));
    }, [id.expenseId]);


    const putExpense = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        fetch(`${process.env.REACT_APP_BASE_URL}/expenses/${id.expenseId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: id.expenseId,
                purpose: purpose,
                description: description,
                amount: amount,
                currency: currency
            })
        })
            .then(clearForm)
            .then(() => nav('/expenses'));
    }

    function cancelEdit() {
        clearForm();
        nav('/expenses');
    }

    return (
        <div>
            <h2>Ausgabe bearbeiten</h2>
            {error && <h4>{error}</h4>}

            <form onSubmit={ev => putExpense(ev)}>
                <input type="text" placeholder={'Bezeichnung'} value={purpose} required
                       onChange={ev => setPurpose(ev.target.value)}/>
                <input type="text" placeholder={'Beschreibung'} value={description}
                       onChange={ev => setDescription(ev.target.value)}/>
                <input type="text" placeholder={'Betrag'} value={amount} required
                       onChange={ev => setAmount(ev.target.value)}/>

                <select value={currency}
                        onChange={ev => setCurrency(ev.target.value)} required>
                    <option value={"EUR"}>Euro</option>
                    <option value={"USD"}>US-Dollar</option>
                    <option value={"GBP"}>Britisches Pfund</option>
                    <option value={"CHF"}>Schweizer Franken</option>
                    <option value={"JPY"}>Yen</option>
                </select>
                <button id={"edit-button"} type="submit"> &#10004; ändern</button>
            </form>

            <button id={"cancel-button"} type="submit" onClick={event => cancelEdit()}> abbrechen</button>
        </div>
    )
}

export default EditExpense;

