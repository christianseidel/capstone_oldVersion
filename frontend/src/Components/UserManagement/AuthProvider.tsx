import {useNavigate} from "react-router-dom";
import {useContext, useEffect, useState} from "react";
import AuthContext from "./AuthContext";

interface Token {
    token: string;
}

interface Param {
    children?: any;
}

export default function AuthProvider({children}: Param) {

    const [token, setToken] = useState(localStorage.getItem('jwt') ?? '');

    const navigate = useNavigate();

    useEffect(() =>{
        localStorage.setItem('jwt', token);
    }, [token])

    const register = (username: string, password: string, passwordAgain: string) => {
        return fetch (`${process.env.REACT_APP_BASE_URL}/api/register`, {
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
    };

    const login = (username: string, password: string) => {
        return fetch(`${process.env.REACT_APP_BASE_URL}/api/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        })
            .then(response => {
                if (response.status === 401 || response.status === 403) {
                    throw Error ('Benutzername oder Passwort ist nicht korrekt');
                }
                return response.json();
            })
            .then((token: Token) => setToken(token.token))
            .then(() => console.log(`${token}`))
            .then(() => console.log('wieso ist das Token leer?'))
            .then(() => localStorage.setItem('jwt2AuthProvider', token))
            .then(() => localStorage.setItem('username', username))
    };

    const logout = () => {
        setToken('');
        localStorage.removeItem('jwt');
        localStorage.removeItem('username');
        navigate("/users/logout");
    };

    return (
        <AuthContext.Provider
            value={{
                token,
                register,
                login,
                logout
            }} >
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => useContext(AuthContext)