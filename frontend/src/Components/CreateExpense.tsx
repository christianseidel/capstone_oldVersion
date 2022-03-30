import {FormEvent, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";

function CreateExpense() {

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


    function clearForm() {
        localStorage.setItem('purpose', '');
        localStorage.setItem('description', '');
        localStorage.setItem('amount', '');
        localStorage.setItem('currency', '');
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
            .then(() => clearForm());
        nav('/expenses');
    }


    return (
        <div>
            <form onSubmit={ev => postExpense(ev)}>
                <input type="text" placeholder={'Purpose'} value={purpose} required
                       onChange={ev => setPurpose(ev.target.value)}/>
                <input type="text" placeholder={'Description'} value={description}
                       onChange={ev => setDescription(ev.target.value)}/>
                <input type="text" placeholder={'Amount'} value={amount} required
                       onChange={ev => setAmount(ev.target.value)}/>
                <input type="text" placeholder={'Euro'} value={currency} required
                       onChange={ev => setCurrency(ev.target.value)}/>
                <button type="submit"> &#10004; erstellen</button>
            </form>
        </div>
    )
}

export default CreateExpense;

/*               Ein Auswahlmenü für "Currency" habe ich erstmal nicht hingekriegt:
                <select value={currency}
                       onChange={ev => setCurrency(ev.target.value)}>
                    <option value={"Euro"}>Euro</option>
                    <option value={"USDollar"}> US-Dollar</option>
                </select>
                */

