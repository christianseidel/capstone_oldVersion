import React, {FormEvent, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import './expenses.css'
import i18n from "i18next"
import {useAuth} from "./UserManagement/AuthProvider";
import {useTranslation} from "react-i18next";
import deFlag from "../Media/Images/de.png";
import enFlag from "../Media/Images/en.png";

function CreateExpense() {

    const nav = useNavigate();
    const {token} = useAuth();
    const {t} = useTranslation();

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
        fetch(`${process.env.REACT_APP_BASE_URL}/api/expenses`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
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
                <h2>{t('create-item-page_title')}</h2>
                <span><img
                    src={(localStorage.getItem('i18nextLng') === 'en') ? deFlag : enFlag} width={'28px'} height={'28px'}
                    alt={'set to English / Deutsch auswählen'} onClick={() => setLanguage()}/>
                </span>
            </div>
            <form onSubmit={ev => postExpense(ev)}>
                <input type="text" placeholder={t('input-form_designation')} value={purpose} required
                       onChange={ev => setPurpose(ev.target.value)}/>
                <input type="text" placeholder={t('input-form_description')} value={description}
                       onChange={ev => setDescription(ev.target.value)}/>
                <input type="text" placeholder={t('input-form_amount')} value={amount} required
                       onChange={ev => setAmount(ev.target.value)}/>

                <select value={currency}
                        onChange={ev => setCurrency(ev.target.value)}>
                    <option value={"EUR"}>{t('currency-form_EUR')}</option>
                    <option value={"USD"}>{t('currency-form_USD')}</option>
                    <option value={"GBP"}>{t('currency-form_GBP')}</option>
                    <option value={"CHF"}>{t('currency-form_CHF')}</option>
                    <option value={"JPY"}>{t('currency-form_JPY')}</option>
                </select>
                <button id={"edit-button"} type="submit"> &#10004; {t('button_create')}</button>
            </form>
            <button id={"cancel-button"} type="submit" onClick={cancelEdit}> &#10008; {t('button_cancel')}</button>
        </div>
    )
}

export default CreateExpense;

