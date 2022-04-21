import {User} from "../model";

/*
interface UserItemProps {
    user: string
}
*/
interface BalanceProps {
    userFrom: string;
    userTo: string;
    balance: number;
}

function UserItem(props: BalanceProps) {

    return (
 /*       <div> propsUser: UserItemProps,
            {propsUser.user}
        </div>
   */
        <div>
            <span>{props.userFrom}</span> <span>{props.userTo}</span>: <span>{props.balance}</span>
        </div>
    )

}

export default UserItem