import React from "react";
import {TransactionsDTO} from "../model";

interface BalanceProps {
    data: TransactionsDTO;
}

function BalanceItem(props: BalanceProps) {

    return (
        <div className={'balanceItem_wrapper'}>
            <div className={'balanceItem'}>
                <span>&#9679;&nbsp;{props.data.userFrom}</span>&nbsp;
                <span>to {props.data.userTo}</span>:<br/>&nbsp; &nbsp;
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