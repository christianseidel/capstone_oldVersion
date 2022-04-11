import React, {FormEvent, useEffect, useState} from "react";
import {useAuth} from "./AuthProvider";
import {useNavigate} from "react-router-dom";
import icon_eyes from "../../Media/Images/eyes.png";

function LoginUser() {

    const nav = useNavigate();

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

    const clearForm = () => {
        localStorage.setItem('username', '');
    }

    const doLogin = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        login(loginUsername, loginPassword)
            .catch(e => setErrorMessage(e.message))
            .then(clearForm)
            .then(() => nav('/expenses'));
    };


   return (
        <div>
            <h2>Bitte logge dich ein</h2>
            {errorMessage && <h4>{errorMessage}</h4>}

            <form onSubmit={ev => doLogin(ev)}>
                <input type="text" placeholder={'Name'} value={loginUsername} required
                       onChange={ev => setLoginUsername(ev.target.value)}/>
                <input type={showPasswordToggle} placeholder={'Passwort'} value={loginPassword} required
                       onChange={ev => setLoginPassword(ev.target.value)}/>

                <button id={"edit-button"} type="submit"> &#10004; einloggen</button>

            </form>
            <div>
                <button id={"showPassword-button"} type="submit"
                        onClick={() => ((showPasswordToggle==='text')
                            ? setShowPasswordToggle('password')
                            : setShowPasswordToggle("text"))}>
                    <img id={'showPassword-button-icon'} src={icon_eyes} alt={'edit item'} /> &nbsp;
                    Passwort
                    {showPasswordToggle==='text' && <> verbergen</>}
                    {showPasswordToggle==='password' && <> anzeigen</>}
                </button>
            </div>

            <h3>Noch nicht registriert?</h3>
            <div>
                Bitte registriere dich erst für die Nutzung von SmartCount: <button id={"register-button"} onClick={() => nav('/users/register')}>&#10140; registrieren</button>
            </div>


        </div>
    )
}

export default LoginUser;