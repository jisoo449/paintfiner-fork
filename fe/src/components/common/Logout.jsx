import {useResetRecoilState,useRecoilValue} from "recoil";
import {userState} from '../../states/atom';

export default function Logout(){
    const resetUser = useResetRecoilState(userState);
    const user = useRecoilValue(userState);

    if (user.state)
        return <a onClick={resetUser} href="/">로그아웃</a>
    
    return <></>
}