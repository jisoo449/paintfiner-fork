import styles from './Header.module.css';
import useAuthToken from '../../hooks/useAuthToken';

export default function UGIcon(){
    //const  = useResetRecoilState(userState);
    const {user, logout} = useAuthToken();
    const onClickLogout = (e) => {  
        e.preventDefault();
        logout().then(()=>window.location.replace("/"));
    }

    if (user.state){//로그인 상태
        return(
            <div className={styles.icon_box}>
                {/* <a className={styles.icon_string} href='/forum'> 포럼 </a> */}
                <a href="/mypage" > {user.nickname} </a> <a className={styles.icon_string}>님</a>
                <a onClick={onClickLogout} > 로그아웃 </a>
                <a href='/mypalette'> 마이팔레트 </a>
            </div>
            )
    }
    else{
        return(
            <div className={styles.icon_box}>
                <a href='/login'> 로그인 </a>
                {/* <a className={styles.icon_string} href='/forum'> 포럼 </a> */}
            </div>
        )
    }
}
//BsPerson, BsStar, BsBasket3
//<a className={styles.icon_string} href='/compare'>물감비교 </a>
//<a className={styles.icon_string} href='/join'>회원가입</a>
//<a className={styles.icon_string} href='/mypalette'> 마이팔레트 </a>