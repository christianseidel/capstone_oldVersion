import {useNavigate} from "react-router-dom";
import React, {FormEvent, useEffect, useState} from "react";
import icon_eyes from "../../Media/Images/eyes.png";

function CreateUser() {

    const nav = useNavigate();

    const [username, setUsername] = useState(localStorage.getItem('username') ?? '');
    const [passwordAgain, setPasswordAgain] = useState('');
    const [password, setPassword] = useState('');
    const [showPasswordToggle, setShowPasswordToggle] = useState('password');
    const [error, setError] = useState('');


    const clearForm = () => {
        localStorage.setItem('username', '');
    }

    useEffect(() => {
        localStorage.setItem('username', username);
    }, [username]);

    useEffect(() => {
        setError('');}, [])

    const createUser = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        fetch(`${process.env.REACT_APP_BASE_URL}/users`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
            body: JSON.stringify({
                username: username,
                password: password,
                passwordAgain: passwordAgain
                })
        })
            .then(clearForm)
            .then(() => nav('/expenses'));
    }

    function cancelCreation() {
        clearForm();
        nav('/expenses');
    }

    return (
        <div>
            <h2>Nutzer registrieren</h2>
            {error && <h4>{error}</h4>}

            <form onSubmit={ev => createUser(ev)}>
                <input type="text" placeholder={'Name'} value={username} required
                       onChange={ev => setUsername(ev.target.value)}/>
                <input type={showPasswordToggle} placeholder={'Passwort'} value={password} required
                       onChange={ev => setPassword(ev.target.value)}/>
                <input type={showPasswordToggle} placeholder={'Passwort wiederholen'} value={passwordAgain} required
                       onChange={ev => setPasswordAgain(ev.target.value)}/>

                <button id={"edit-button"} type="submit"> &#10004; Nutzer anlegen</button>

            </form>
                <div>
                    <button id={"cancel-button"} type="submit" onClick={event => cancelCreation()}>
                        abbrechen
                    </button>
                    <button id={"showPassword-button"} type="submit"
                            onClick={() => ((showPasswordToggle==='text')
                                ? setShowPasswordToggle('password')
                                : setShowPasswordToggle("text"))}>
                        <img id={'setShowPassword-button'} src={icon_eyes} alt={'edit item'} /> &nbsp;
                        Passwörter
                        {showPasswordToggle==='text' && <> verbergen</>}
                        {showPasswordToggle==='password' && <> anzeigen</>}
                    </button>
                </div>

        </div>
    )
}

export default CreateUser;