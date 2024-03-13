import React from 'react';
import { useNavigate } from 'react-router-dom';
import { ReactComponent as BackIcon } from '../other/b_arr_v2.svg'; // Make sure to import your back arrow icon
import styles from '../style/BackButton.module.css'

const BackButton = () => {
    let navigate = useNavigate();

    const goBack = () => {
        navigate(-1); // This will take the user to the previous page in history
    };

    return (
        <button onClick={goBack} className={styles.backButton}>
            <BackIcon /> {/* This is your back arrow icon */}
        </button>
    );
};

export default BackButton;
