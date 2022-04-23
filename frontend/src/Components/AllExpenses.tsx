import React, {useState, useEffect, useCallback} from 'react';
import {checkLogin, ExpenseDTO} from "./model";
import ExpenseItem from "./ExpenseItem"
import {useNavigate} from "react-router-dom";
import './expenses.css'
import {useAuth} from "./UserManagement/AuthProvider";
import {useTranslation} from "react-i18next";
import deFlag from "../Media/Images/de.png";
import enFlag from "../Media/Images/en.png";
import i18n from "i18next";

function AllExpenses() {

    const nav = useNavigate();
    const {t} = useTranslation();

    const {token, logout} = useAuth();
    const [expensesDTO, setExpensesDTO] = useState({} as ExpenseDTO);
    const [showItemRange, setShowItemRange] = useState(`${t('button_showMyItemsOnly')}`);
    const [iconItemRange, setIconItemRange] = useState('<');
    let loading: String = `${t('message_loading')}`;

    useEffect(() => {
        if (!localStorage.getItem('jwt')) {
            nav('/users/login')
        }
    }, [nav])

    const fetchMyExpensesOnly = useCallback(() => {
        fetch(`${process.env.REACT_APP_BASE_URL}/api/expenses/user`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => response.json())
            .then((responseBody: ExpenseDTO) => setExpensesDTO(responseBody));
    }, [token]);

    useEffect(() => {
        (localStorage.getItem('show') === 'mine') ? fetchMyExpensesOnly() :
            setShowItemRange(`${t('button_showMyItemsOnly')}`);
        fetch(`${process.env.REACT_APP_BASE_URL}/api/expenses`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => {
                checkLogin(response.status);
                return response.json()
            })
            .then((responseBody: ExpenseDTO) => setExpensesDTO(responseBody))
            .catch(() => nav('/users/login'))
    }, [fetchMyExpensesOnly, token, t, nav]);

    const fetchAllExpenses = () => {
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

    const setItemsRange = () => {
        if (localStorage.getItem('show') === 'mine') {
            localStorage.setItem('show', 'all');
            setShowItemRange(`${t('button_showMyItemsOnly')}`);
            setIconItemRange('<');
            fetchAllExpenses();
        } else {
            localStorage.setItem('show', 'mine');
            setShowItemRange(`${t('button_showAllItems')}`);
            setIconItemRange('>');
            fetchMyExpensesOnly();
        }
    }

    const doLogout = () => {
        localStorage.removeItem('show');
        logout();
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

            <div className={'buttons_top-line'}>
                <button id={"showItemRange-button_FrontPage"} onClick={setItemsRange}>
                    <span id={'iconItemRange'}>{iconItemRange}</span>&nbsp;{showItemRange}
                </button>
                <button id={"createItem-button_FrontPage"} onClick={() => nav('/edit')}>
                    <strong>&#65291;</strong> {t('button_goToAddExpense')}
                </button>
            </div>

            <div>
                {expensesDTO.expenses ? expensesDTO.expenses.map(item => <ExpenseItem key={item.id} expense={item}
                                                                                      onItemDeletion={fetchAllExpenses}
                                                                                      onExpenseChange={setExpensesDTO}/>)
                    : <span>{t('message_loading')}</span>}
            </div>

            <div className={"item_wrapper"}>
                <div className={"sum"}>
                {(expensesDTO.sum !== 0) && <span>{t('landing-page_sum')}:</span>}
                <span>{expensesDTO.sum ? (expensesDTO.sum).toLocaleString('de-De', {
                        style: 'currency', currency: 'EUR', minimumFractionDigits: 2  // hard-coded "EUR" will be solved and implemented at a later point in time
                    })
                    : ((expensesDTO.sum === 0) ? <span>{t('landing-page_zeroExpense')}.</span> :
                        <span>{loading}</span>)}</span></div>
            </div>
            <div className={'buttons_first-line'}>

            </div>

            <div className={'buttons_third-line'}>
                <button id={"showUsers-button_FrontPage"}
                        onClick={() => nav('/expenses/balance')}>{t('button_showUserList')}</button>
                <button id={"logout-button_FrontPage"} onClick={doLogout}>{t('button_logOut')}</button>
            </div>

        </div>
    );
}

export default AllExpenses;
