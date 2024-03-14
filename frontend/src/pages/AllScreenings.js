import React, { useState, useEffect } from 'react';
import styles from '../style/AllScreenings.module.css';
import moment from 'moment';
import {useParams} from "react-router-dom";

function AllScreenings() {
    const [allScreenings, setAllScreenings] = useState([]);
    const [screenings, setScreenings] = useState([]);
    const [selectedDay, setSelectedDay] = useState(moment().format('ddd')); // Default to current day of week
    const {id} = useParams();
    const [numberOfReservedSeats, setNumberOfReservedSeats] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/screenings/allScreenings')
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                console.log("Screenings data:", data); // Add this to see the actual structure of response
                if (!Array.isArray(data)) {
                    throw new Error('Data is not an array!');
                }
                setAllScreenings(data);
                filterScreeningsByDay(selectedDay, data);
            })
            .catch(error => {
                console.error('Error fetching screenings:', error);
            });

        fetch(`http://localhost:8080/reservations/howManyReserved/${id}`)
            .then(response => response.json())
            .then(data => setNumberOfReservedSeats(data))
            .catch(error => console.error(error));

    }, []);

    const filterScreeningsByDay = (day, screeningsData = allScreenings) => {
        const filtered = screeningsData.filter((screening) =>
            moment(screening.screening.startTime).format('ddd') === day
        );
        setScreenings(filtered);
    };

    const handleDayButtonClick = (day) => {
        setSelectedDay(day);
        filterScreeningsByDay(day);
    };

    return (
        <div className={styles.allScreenings}>
            <div className={styles.menuBar}>
                {['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'].map((day) => (
                    <button
                        key={day}
                        className={`${styles.dayButton} ${selectedDay === day ? styles.active : ''}`}
                        onClick={() => handleDayButtonClick(day)}
                    >
                        {day}
                    </button>
                ))}
            </div>
            <div className={styles.screeningsList}>
                {screenings.map((screening) => (
                    <div key={screening.screening.id} className={styles.screeningContainer}>
                        <div className={styles.moviePoster} style={{backgroundImage: `url(${screening.movie.picUrl})`}}>
                            {/* Image will be displayed via background */}
                        </div>
                        <div className={styles.movieInfoAndButtons}>
                            <div className={styles.movieInfo}>
                                <h2>{screening.movie.movieName}</h2>
                                <h3> {moment(screening.screening.startTime).format('LLL')} </h3>
                                <p>Rating: {screening.movie.rating}</p>
                                <p>Room: {screening.room.room_name}</p>
                                <p>Available Seats: {screening.room.totalSeats - numberOfReservedSeats} / {screening.room.totalSeats}</p>
                            </div>
                            <div className={styles.buttonGroup}>
                                <button className={styles.screeningButton}>Get tickets</button>
                                <button className={styles.screeningButton}>View movie</button>
                                <button className={styles.screeningButton}>Schedule</button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>

        </div>
    );
}

export default AllScreenings;
