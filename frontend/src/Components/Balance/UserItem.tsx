import React from "react";
import {TransactionsDTO} from "../model";

interface BalanceProps {
    data: TransactionsDTO;
}

function UserItem(props: BalanceProps) {

    return (
        <div>
            <span>&#9679;&nbsp;{props.data.userFrom}</span>&nbsp;
            <span>to {props.data.userTo}</span>:<br/>&nbsp; &nbsp;&
            <span>{props.data.balance}</span>
        </div>
    )
}

export default UserItem