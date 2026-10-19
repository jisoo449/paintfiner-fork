import styles from "./Forum.module.css"
import React, { useEffect } from 'react';
import ForumListItem from '../components/common/ForumListItem';

export default function Forum() {

    return (
      <div className={styles.wrapper_forum}>
        <p>여기 포럼이다!!! - 어느페이지인지 알려고 나중에 지울꺼</p>
        <ForumListItem/>
      </div>
    );
}