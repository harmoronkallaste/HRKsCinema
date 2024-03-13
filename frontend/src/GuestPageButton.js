import React from 'react';
import { useNavigate } from 'react-router-dom'; // Update import to useNavigate

function GuestPageButton() {
    const navigate = useNavigate(); // Update to useNavigate instead of useHistory

    const redirectToGuestPage = () => {
        navigate('/guestPage');
    };

    return (
        <button onClick={redirectToGuestPage} style={{ position: 'fixed', top: '10px', left: '10px', zIndex: '9999' }}>
            Guest Page
        </button>
    );
}

export default GuestPageButton;
