import styles from './Comments.module.css';
import { userState } from '../../states/userAtom';
import { testUserState } from '../../states/testAtom';
import { useRecoilValue } from 'recoil';
import { commentCaution } from '../../constants/placeholder.constants';
import { useState } from 'react';

export default function Comments({comments}){

    const user = useRecoilValue(testUserState);
    const [comment, setComment] = useState('');

    const onChangeComment = e => {
        setComment(e.target.value);
    }

    return(
        <div className={styles.wrapper_comments}>
            <h3>댓글 ({comments?Object.keys(comments).length:0})</h3>

            {user.state===true && 
                <div className={styles.input_comment}>
                    <p className={styles.nickname}>{user.nickname}</p>
                    <input type="text" value={comment} placeholder={commentCaution} onChange={onChangeComment}/>
                    <p className={styles.limit}>{comment.length}/500</p>
                    <div className={styles.holder_btn}><button>등록</button></div>
                </div>
            }
            
            <div className={styles.list_comments}>
                {comments && (<div>asd</div>)}
            </div>
            
        </div>
    )
}