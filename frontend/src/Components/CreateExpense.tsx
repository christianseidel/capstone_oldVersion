import {FormEvent, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";


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
                <input type="text" placeholder={'Purpose'} value={purpose} required
                       onChange={ev => setPurpose(ev.target.value)}/>
                <input type="text" placeholder={'Description'} value={description}
                       onChange={ev => setDescription(ev.target.value)}/>
                <input type="text" placeholder={'Amount'} value={amount} required
                       onChange={ev => setAmount(ev.target.value)}/>

                <select value={currency}
                        onChange={ev => setCurrency(ev.target.value)}>
                    <option value={"EUR"}>Euro</option>
                    <option value={"USD"}>US-Dollar</option>
                    <option value={"GBP"}>Britisches Pfund</option>
                    <option value={"CHF"}>Schweizer Franken</option>
                    <option value={"JPY"}>Yen</option>
                </select>
                <button type="submit"> &#10004; erstellen</button>
            </form>

            <button type="submit" onClick={event => cancelEdit()}> abbrechen</button>
        </div>
    )
}

export default CreateExpense;

