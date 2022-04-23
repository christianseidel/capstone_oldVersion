import {useNavigate} from "react-router-dom";
import {useAuth} from "../UserManagement/AuthProvider";
import {useTranslation} from "react-i18next";
import React, {useEffect, useState} from "react";
import deFlag from "../../Media/Images/de.png";
import enFlag from "../../Media/Images/en.png";
import i18n from "i18next";
import {TransactionsDTO} from "../model";
import BalanceItem from "./BalanceItem";


function BalanceList() {

    const nav = useNavigate();
    const {token} = useAuth();
    const {t} = useTranslation();

    const [userList, setUserList] = useState([] as Array<string>)
    const [transactions, setTransactions] = useState([] as Array<TransactionsDTO>)
    /*    let loading : String = `${t('message_loading')}`; */

    useEffect(() => {
        if (!localStorage.getItem('jwt')) {
            nav('/users/login')
        }
    }, [nav])


    useEffect(() => {
        fetch(`${process.env.REACT_APP_BASE_URL}/api/expenses/balance`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => response.json())
            .then((responseBody: Array<TransactionsDTO>) => setTransactions(responseBody));
    }, [token]);

    useEffect(() => {
        fetch(`${process.env.REACT_APP_BASE_URL}/api/expenses/userlist`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => response.json())
            .then((responseBody: Array<string>) => setUserList(responseBody));
    }, [token]);


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
                <h2>{t('list-of-users-page_title')}</h2>
                <span><img
                    src={(localStorage.getItem('i18nextLng') === 'en') ? deFlag : enFlag} width={'28px'} height={'28px'}
                    alt={'set to English / Deutsch auswählen'} onClick={() => setLanguage()}/>
                </span>
            </div>
            {userList.map(item => <div key={item}> {item}</div>)}
            <p></p>
            <div className={'explain'}>The members of this group need to make the following payments in order to settle all balances:</div>
            <div>
                {transactions.map(item => <BalanceItem key={item.userFrom} data={item} />

                )}
            </div>

            <div className={'buttons_first-line'}>
                <button className={'button_bellow-list'} onClick={() => nav('/expenses')}>{t('button_showCashbook')}</button>
            </div>

        </div>
    )
}

export default BalanceList;
