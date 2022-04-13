import React, {useState, useEffect} from 'react';
import {ExpenseDTO} from "./model";
import ExpenseItem from "./ExpenseItem"
import {useNavigate} from "react-router-dom";
import './expenses.css'
import i18n, {t} from "i18next";
import {useAuth} from "./UserManagement/AuthProvider";
import {useTranslation} from "react-i18next";
import deFlag from "../Media/Images/de.png";
import enFlag from "../Media/Images/en.png";

function AllExpenses() {

    const nav = useNavigate();
    const {t} = useTranslation();

    const {token, logout} = useAuth();
    const [expensesDTO, setExpensesDTO] = useState({} as ExpenseDTO);
    let loading : String = 'loading ...'


    useEffect(() => {
        if (!localStorage.getItem('jwt')) {
            nav('/users/login')
        }
    }, [nav])

    useEffect(() => {
        fetch(`${process.env.REACT_APP_BASE_URL}/api/expenses`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => response.json())
            .then((responseBody: ExpenseDTO) => setExpensesDTO(responseBody));
            }, [token]);

    const fetchAllExpenses= () => {
        fetch(`${process.env.REACT_APP_BASE_URL}/api/expenses`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => response.json())
            .then((responseBody: ExpenseDTO) => setExpensesDTO(responseBody));
    };

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
                <h1>SmartCount &ndash; {t('landing-page_title')}</h1>
                <span><img
                    src={(localStorage.getItem('i18nextLng') === 'en') ? deFlag : enFlag} width={'28px'} height={'28px'}
                    alt={'set to English / Deutsch auswählen'} onClick={() => setLanguage()}/>
                </span>

            </div>

            <div>
                {expensesDTO.expenses ? expensesDTO.expenses.map(item => <ExpenseItem key={item.id} expense={item}
                                                                                onItemDeletion={fetchAllExpenses}
                                                                                onExpenseChange={setExpensesDTO}/>)
                    : <span>{loading}</span>}
            </div>

            <div className={"sum"}>
                {(expensesDTO.sum !== 0) && <span>{t('landing-page_sum')}:</span>}
                <span>{expensesDTO.sum ? (expensesDTO.sum).toLocaleString('de-De', {
                style: 'currency', currency: 'EUR', minimumFractionDigits: 2  // hard-coded "EUR" will be solved and implemented at a later point in time
            })
                : ((expensesDTO.sum === 0) ? <span>Es wurden noch keine Ausgaben erfasst.</span> : <span>{loading}</span>)}</span></div>

            <div>
                <button id={"create-button_FrontPage"} onClick={() => nav('/edit')}>&#65291; {t('button_goToAddExpense')}</button>
            </div>
            <p></p>
            <div>
                <button id={"createUser-button_FrontPage"} onClick={() => logout()}>{t('button_logOut')}</button>
            </div>

        </div>
    );
}

export default AllExpenses;
