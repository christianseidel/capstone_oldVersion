import {User} from "../model";

interface UserItemProps {
    user: string
}

function UserItem(props: UserItemProps) {

    return (
        <div>
            {props.user}
        </div>
    )

}

export default UserItem