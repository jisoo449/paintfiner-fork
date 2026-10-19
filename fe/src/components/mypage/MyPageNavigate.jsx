import styles from './MyPageNavigate.module.css'

export default function MyPageNavigate({ visit, setVisit }) {

    return(
        <div className={styles.container}>
            <div className={styles.title}>마이페이지</div>

            <div>
                <div className={styles.navItem} onClick={() => setVisit(1)}>회원정보 변경</div>
                {visit === 1 && <div className={styles.selected}>{"<"}</div>}
            </div>
            <div>
                <div className={styles.navItem} onClick={() => setVisit(2)}>내 댓글 관리</div>
                {visit === 2 && <div className={styles.selected}>{"<"}</div>}
            </div>
        </div>
    )

}