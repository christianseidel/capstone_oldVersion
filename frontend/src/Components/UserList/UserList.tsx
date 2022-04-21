import {useNavigate} from "react-router-dom";
import {useAuth} from "../UserManagement/AuthProvider";
import {useTranslation} from "react-i18next";
import React, {useEffect, useState} from "react";
import deFlag from "../../Media/Images/de.png";
import enFlag from "../../Media/Images/en.png";
import i18n from "i18next";


function UserList() {

    const nav = useNavigate();
    const {token} = useAuth();
    const {t} = useTranslation();

    const [userList, setUserList] = useState([] as Array<string>)
    /*    let loading : String = `${t('message_loading')}`; */

    useEffect(() => {
        if (!localStorage.getItem('jwt')) {
            nav('/users/login')
        }
    }, [nav])

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
            {userList.map(item => <div> {item}</div>)}
            <div>

            </div>

            <div className={'buttons_first-line'}>
                <button onClick={() => nav('/expenses')}>{t('button_showCashbook')}</button>
            </div>

        </div>
    )
}

export default UserList;
