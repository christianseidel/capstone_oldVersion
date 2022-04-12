import {useNavigate} from "react-router-dom";
import React, {FormEvent, useEffect, useState} from "react";
import {useAuth} from "./AuthProvider";
import icon_eyes from "../../Media/Images/eyes.png";

function CreateUser() {

    const nav = useNavigate();

    const [username, setUsername] = useState(localStorage.getItem('username') ?? '');
    const [passwordAgain, setPasswordAgain] = useState('');
    const [password, setPassword] = useState('');
    const [showPasswordToggle, setShowPasswordToggle] = useState('password');
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        if (localStorage.getItem('jwt')) {
            nav('/expenses')
        }
    }, [nav])

    useEffect(() => {
        localStorage.setItem('username', username);
    }, [username]);

    useEffect(() => {
        setErrorMessage('');}, [])

    const {register} = useAuth();

    function doRegister(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
       register(username, password, passwordAgain)
            .then(response => {
                if (response.status === 400) {
                    setErrorMessage('Die Passwörter stimmen nicht überein.');
                } else if (response.status === 409) {
                    setErrorMessage('Der gewählte Benutzername ist bereits vergeben.');
                }
            })
            .then(clearForm)
            .then(() => nav('/expenses'));  // I still need to check and adapt this ////////////////////////////////
    }

    const clearForm = () => {
        localStorage.setItem('username', '');
    }

    function cancelCreation() {
        clearForm();
        nav('/users/login');
    }

    // I still need to implement error message -- none is shown as of now /////
    return (
        <div>
            <h2>Nutzer registrieren</h2>
            {errorMessage && <div className="error">{errorMessage}</div>}

            <form onSubmit={ev => doRegister(ev)}>
                <input type="text" placeholder={'Name'} value={username} required
                       onChange={ev => setUsername(ev.target.value)}/>
                <input type={showPasswordToggle} placeholder={'Passwort'} value={password} required
                       onChange={ev => setPassword(ev.target.value)}/>
                <input type={showPasswordToggle} placeholder={'Passwort wiederholen'} value={passwordAgain} required
                       onChange={ev => setPasswordAgain(ev.target.value)}/>

                <button id={"edit-button"} type="submit"> &#10004; anlegen</button>

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

            <div>
                <button id={"cancel-button"} type="submit" onClick={event => cancelCreation()}>
                    &#10008; abbrechen
                </button>
            </div>

        </div>
    )
}

export default CreateUser;