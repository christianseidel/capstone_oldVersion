import React from "react";
import {TransactionsDTO} from "../model";
import {useTranslation} from "react-i18next";

interface BalanceProps {
    data: TransactionsDTO;
}

function BalanceItem(props: BalanceProps) {

    const {t} = useTranslation();

    return (
        <div className={'balanceItem_wrapper'}>
            <div className={'balanceItem'}>
                <span>&#9679;&nbsp;{props.data.userFrom} {t('to')} {props.data.userTo}:</span>
                <span>{(props.data.balance).toLocaleString('de-De', {
                    style: 'currency',
                    currency: props.data.currency,
                    minimumFractionDigits:2
                })}</span>
            </div>
        </div>
    )
}

export default BalanceItem