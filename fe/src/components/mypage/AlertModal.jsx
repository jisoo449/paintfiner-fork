import { useState } from 'react';
import styles from './AlertModal.module.css'

export default function AlertModal({showModal, setShowModal, message}) {
    const closeModal = () => {
        setShowModal(false);
    };

    return (
        <>{showModal && (
            <div className={styles.modalOverlay}>
                <div className={styles.modal}>
                    <div className={styles.modalMessage}>{message}</div>
                    <button onClick={closeModal} className={styles.modalButton}>확인</button>
                </div>
            </div>
        )}</>
    )
}