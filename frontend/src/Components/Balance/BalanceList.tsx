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
                <h2>{t('balances_title')}</h2>
                <span><img
                    src={(localStorage.getItem('i18nextLng') === 'en') ? deFlag : enFlag} width={'28px'} height={'28px'}
                    alt={'set to English / Deutsch auswählen'} onClick={() => setLanguage()}/>
                </span>
            </div>

            <div className={'balancePage'}>
                <div className={'balancePage_paneLeft'}>
                    <div className={'balancePage_explained'}>{t('balances_explained')}</div>
                </div>
                <div className={'balancePage_paneRight_alignmentBottom'}>
                    <div className={'userList_explained'}>{t('userList')}</div>
                </div>
            </div>

            <div className={'balancePage'}>
                <div className={'balancePage_paneLeft'}>
                    <div>
                    {transactions.map(item => <BalanceItem key={item.userFrom} data={item} />)}
                    </div>
                    <div className={'buttons_first-line'}>
                        <button className={'button_bellow-list'} onClick={() => nav('/expenses')}>{t('button_showCashbook')}</button>
                    </div>
                </div>
                <div className={'balancePage_paneRight'}>
                    <div className={'userList'}>{userList.map(item => <div key={item}>&#9679;&nbsp; {item}</div>)}</div>
                </div>
            </div>
        </div>
    )
}

export default BalanceList;
