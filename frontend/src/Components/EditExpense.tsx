import React, {FormEvent, useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {Expense} from "./model";
import './expenses.css'
import {useAuth} from "./UserManagement/AuthProvider";
import i18n from "i18next";
import {useTranslation} from "react-i18next";
import deFlag from "../Media/Images/de.png";
import enFlag from "../Media/Images/en.png";

function EditExpense() {

    const nav = useNavigate();
    const {token} = useAuth();
    const {t} = useTranslation();

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
        fetch(`${process.env.REACT_APP_BASE_URL}/api/expenses/${id.expenseId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
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
    }, [token, id.expenseId]);


    const putExpense = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        fetch(`${process.env.REACT_APP_BASE_URL}/api/expenses/${id.expenseId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
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

    function setLanguage() {
        if (localStorage.getItem('i18nextLng') === 'en') {
            i18n.changeLanguage('de');
        } else {
            i18n.changeLanguage('en');
        }
    }

    return (
        <div>
            <div className={'heading'}>
                <h2>{t('edit-item-page_title')}</h2>
                <span><img
                    src={(localStorage.getItem('i18nextLng') === 'en') ? deFlag : enFlag} width={'28px'} height={'28px'}
                    alt={'set to English / Deutsch auswählen'} onClick={() => setLanguage()}/>
                    </span>
            </div>

            {error && <h4>{error}</h4>}

            <form onSubmit={ev => putExpense(ev)}>
                <input type="text" placeholder={t('input-form_designation')} value={purpose} required
                       onChange={ev => setPurpose(ev.target.value)}/>
                <input type="text" placeholder={t('input-form_description')} value={description}
                       onChange={ev => setDescription(ev.target.value)}/>
                <input type="text" placeholder={t('input-form_amount')} value={amount} required
                       onChange={ev => setAmount(ev.target.value)}/>

                <select value={currency}
                        onChange={ev => setCurrency(ev.target.value)} required>
                    <option value={"EUR"}>{t('currency-form_EUR')}</option>
                    <option value={"USD"}>{t('currency-form_USD')}</option>
                    <option value={"GBP"}>{t('currency-form_GBP')}</option>
                    <option value={"CHF"}>{t('currency-form_CHF')}</option>
                    <option value={"JPY"}>{t('currency-form_JPY')}</option>
                </select>
                <button id={"edit-button"} type="submit"> &#10004; {t('button_change')}</button>
                <div>
                    <button id={"cancel-button"} type="submit" onClick={cancelEdit}> &#10008; {t('button_cancel')} </button>
                </div>
            </form>
        </div>
    )
}

export default EditExpense;

