// ScreeningInfoComponent.js

import React, { useState, useEffect } from 'react';
import axios from 'axios';

const ScreeningInfoComponent = () => {
    const [screeningInfo, setScreeningInfo] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchScreeningInfo = async () => {
            try {
                const response = await axios.get('http://localhost:8080/screenings/idsearch/1'); // Replace 1 with the actual screening ID
                setScreeningInfo(response.data);
                setLoading(false);
            } catch (error) {
                console.error('Error fetching screening info:', error);
                setLoading(false);
            }
        };

        fetchScreeningInfo();
    }, []);

    return (
        <div>
            {loading ? (
                <p>Loading...</p>
            ) : (
                <div>
                    <h2>Screening Information</h2>
                    {screeningInfo && (
                        <div>
                            <p>Movie ID: {screeningInfo.movieId}</p>
                            <p>Room ID: {screeningInfo.roomId}</p>
                            {/* Render other screening information as needed */}
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

export default ScreeningInfoComponent;
