import React from 'react';
import styles from './Footer.module.css'

export default function Footer(){
    return (
        <div className={styles.footerwrapper}>
            <div className={styles.footer_sections}>
                <div className={styles.section1}>
                    <h3 className={styles.brandname}>Paint Finder</h3>
                </div>
                <div className={styles.section2}>
                    {/* <div>인스타그램:</div>
                    <div>인스타그램:</div> */}
                </div>
                <div className={styles.section3}></div>
            </div>
        </div>
    );
}