import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import React from "react";

function LogoutUser() {

    const nav = useNavigate();
    const {t} = useTranslation();

    return (
        <div>
            <div>
                <h3>{t("logout-page_title")}</h3>
            </div>


            <br/>
            <div>
                {t('logout-page_backToLogin')}:  <button id={"login-button"} onClick={() => nav('/users/login')}>&#10140; {t('button_goToLogin')}</button>
            </div>
        </div>
    );
}

export default LogoutUser;
