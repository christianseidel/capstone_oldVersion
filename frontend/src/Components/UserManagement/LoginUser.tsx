import React, {FormEvent, useEffect, useState} from "react";
import {useAuth} from "./AuthProvider";
import {useNavigate} from "react-router-dom";
import icon_eyes from "../../Media/Images/eyes.png";
import enFlag from "../../Media/Images/en.png";
import deFlag from "../../Media/Images/de.png";
import i18n from "i18next";
import {useTranslation} from "react-i18next";

function LoginUser() {

    const nav = useNavigate();
    const {t} = useTranslation();

    const [loginUsername, setLoginUsername] = useState('');
    const [loginPassword, setLoginPassword] = useState('');
    const [showPasswordToggle, setShowPasswordToggle] = useState('password');
    const [errorMessage, setErrorMessage] = useState('');

    const {login} = useAuth();

    useEffect(() => {
        if (localStorage.getItem('jwt')) {
            nav('/expenses')
        }
    }, [nav])

    const doLogin = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        login(loginUsername, loginPassword)
            .then(() => nav('/expenses'))
            .catch(e => setErrorMessage(e.message));
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
                <h2>{t('login-page_title')}</h2>
                <span><img
                    src={(localStorage.getItem('i18nextLng') === 'en') ? deFlag : enFlag} width={'28px'} height={'28px'}
                    alt={'set to English / Deutsch auswählen'} onClick={() => setLanguage()}/>
                </span>
            </div>

            {errorMessage && <h4>{errorMessage}</h4>}

            <form onSubmit={ev => doLogin(ev)}>
                <input type="text" placeholder={t('input-form_name')} value={loginUsername} autoFocus required
                       onChange={ev => setLoginUsername(ev.target.value)}/>
                <input type={showPasswordToggle} placeholder={t('input-form_password')} value={loginPassword} required
                       onChange={ev => setLoginPassword(ev.target.value)}/>

                <button id={"edit-button"} type="submit"> &#10004; {t('button_logIn')}</button>
            </form>

            <div>
                <button id={"showPassword-button"} type="submit"
                        onClick={() => ((showPasswordToggle==='text')
                            ? setShowPasswordToggle('password')
                            : setShowPasswordToggle("text"))}>
                    <img id={'showPassword-button-icon'} src={icon_eyes} alt={'edit item'} /> &nbsp;
                    {showPasswordToggle==='text' && <> {t('button_hidePassword')}</>}
                    {showPasswordToggle==='password' && <> {t('button_showPassword')}</>}
                </button>
            </div>

            <h3>{t('login-page_subtitle')}?</h3>
            <div>
                {t('login-page_pleaseRegister')}: <button id={"register-button"} onClick={() => nav('/users/register')}>&#10140; {t('button_goToRegister')}</button>
            </div>

        </div>
    )
}

export default LoginUser;