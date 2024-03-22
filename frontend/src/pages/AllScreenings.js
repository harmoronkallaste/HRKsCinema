import React, { useState, useEffect } from 'react';
import styles from '../style/AllScreenings.module.css';
import moment from 'moment';
import {useNavigate} from "react-router-dom";

function AllScreenings() {

    const navigate = useNavigate();

    const [screenings, setScreenings] = useState([]);
    const [selectedDay, setSelectedDay] = useState(moment().format('ddd'));
    const [selectedGenres, setSelectedGenres] = useState([]);
    const [selectedTimes, setSelectedTimes] = useState([]);

    // Function to toggle times
    const toggleTime = (newTime) => {
        let updatedTimes = [...selectedTimes];

        if (updatedTimes.length === 2) {
            const [firstTime, secondTime] = updatedTimes.sort((a, b) => moment(a, "HH:mm").diff(moment(b, "HH:mm")));
            const firstTimeMoment = moment(firstTime, "HH:mm");
            const secondTimeMoment = moment(secondTime, "HH:mm");
            const newTimeMoment = moment(newTime, "HH:mm");

            if (newTimeMoment.isAfter(firstTimeMoment) && newTimeMoment.isBefore(secondTimeMoment)) {
                // If the new time falls between the two selected times, deselect the two and select the new one
                updatedTimes = [newTime];
            } else if (updatedTimes.includes(newTime)) {
                // If the new time is already selected (and not between, due to the sort), toggle it off
                updatedTimes = updatedTimes.filter(time => time !== newTime);
            } else {
                // If the new time is outside the range, proceed with standard toggle logic
                updatedTimes.push(newTime);
            }
        } else if (updatedTimes.includes(newTime)) {
            // If less than 2 times are selected and the new time is already selected, toggle it off
            updatedTimes = updatedTimes.filter(time => time !== newTime);
        } else {
            // If less than 2 times are selected and the new time is not already selected, add it
            updatedTimes.push(newTime);
        }

        // Limit selection to at most 2 times by keeping the earliest and latest if more than 2 times are selected
        if (updatedTimes.length > 2) {
            updatedTimes.sort((a, b) => moment(a, "HH:mm").diff(moment(b, "HH:mm")));
            updatedTimes = [updatedTimes[0], updatedTimes[updatedTimes.length - 1]];
        }

        setSelectedTimes(updatedTimes);
    };

    const toggleGenre = (genre) => {
        setSelectedGenres(currentGenres =>
            currentGenres.includes(genre) ? currentGenres.filter(g => g !== genre) : [...currentGenres, genre]
        );
    };

    useEffect(() => {
        fetch('http://localhost:8080/screenings/allScreenings')
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                const screeningsWithGenres = data.map(screening => ({
                    ...screening,
                    genres: screening.movie.genre.split(',').map(genre => genre.trim()),
                    availableSeats: null
                }));

                return Promise.all(screeningsWithGenres.map(screening =>
                    fetch(`http://localhost:8080/reservations/howManyReserved/${screening.screening.id}`)
                        .then(res => res.json())
                        .then(numberOfReservedSeats => ({
                            ...screening,
                            availableSeats: screening.room.totalSeats - numberOfReservedSeats
                        }))
                        .catch(error => {
                            console.error('Error fetching reserved seats:', error);
                            return { ...screening, availableSeats: null };
                        })
                ));
            })
            .then(updatedScreenings => {
                setScreenings(updatedScreenings);
            })
            .catch(error => {
                console.error('Error fetching screenings:', error);
            });
    }, [selectedDay, selectedGenres, selectedTimes]);

    const genreButtons = ['Drama', 'Crime', 'Action', 'Biography', 'History'];
    const timeButtons = ["11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00"];

    // Filtered screenings logic
    const filteredScreenings = screenings.filter(screening => {
        const screeningStartTime = moment(screening.screening.startTime);
        const dayMatch = screeningStartTime.format('ddd') === selectedDay;
        const genreMatch = selectedGenres.length === 0 || selectedGenres.some(genre => screening.genres.includes(genre));

        let timeMatch = selectedTimes.length === 0; // Default to true if no time selected, false otherwise
        if (selectedTimes.length > 0) {
            const screeningTimeOnly = screeningStartTime.format("HH:mm");
            const screeningMomentTimeOnly = moment(screeningTimeOnly, "HH:mm");
            if (selectedTimes.length === 1) {
                const singleTime = moment(selectedTimes[0], "HH:mm");
                timeMatch = screeningMomentTimeOnly.isSameOrAfter(singleTime);
            } else {
                const timesMoments = selectedTimes.map(time => moment(time, "HH:mm"));
                const earliestTime = moment.min(timesMoments);
                const latestTime = moment.max(timesMoments);
                timeMatch = screeningMomentTimeOnly.isBetween(earliestTime, latestTime, undefined, '[]');
            }
        }

        return dayMatch && genreMatch && timeMatch;
    });


    return (

        <div className={styles.allScreenings}>

            <div className={styles.menuBar}>

                <div className={styles.filtersTitle}> Filters </div>

                <div className={styles.buttonContainer}>

                    <div className={styles.dayOptions}>
                        {['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'].map((day) => (
                            <button
                                key={day}
                                className={`${styles.dayButton} ${selectedDay === day ? styles.active : ''}`}
                                onClick={() => setSelectedDay(day)}
                            >
                                {day}
                            </button>

                        ))}
                    </div>

                    <div className={styles.timeOptions}>
                        {timeButtons.map(time => (
                            <button
                                key={time}
                                className={`${styles.dayButton} ${selectedTimes.includes(time) ? styles.active : ''}`}
                                onClick={() => toggleTime(time)}
                            >
                                {time}
                            </button>
                        ))}
                    </div>

                    <div className={styles.genreOptions}>
                        {genreButtons.map((genre) => (
                            <button
                                key={genre}
                                className={` ${styles.dayButton} ${styles.genreButton} ${selectedGenres.includes(genre) ? styles.active : ''}`}
                                onClick={() => toggleGenre(genre)}
                            >
                                {genre}
                            </button>
                        ))}
                    </div>
                </div>

            </div>

            <div className={styles.screeningsList}>

                {filteredScreenings.map(screening => (
                    <div key={screening.screening.id} className={styles.screeningContainer}>

                        <div className={styles.moviePoster} style={{backgroundImage: `url(${screening.movie.picUrl})`}}>
                            {/* Image will be displayed via background */}
                        </div>

                        <div className={styles.movieInfo}>

                            <h2>{screening.movie.movieName}</h2>
                            <h3>{moment(screening.screening.startTime).format('dddd HH:mm, MMM Do YYYY')}</h3>
                            <p>Rating: {screening.movie.rating}</p>
                            <p>Room: {screening.room.room_name}</p>
                            <p>Available Seats: {screening.availableSeats !== null ? `${screening.availableSeats} / ${screening.room.totalSeats}` : 'Loading...'}</p>

                        </div>

                        <div className={styles.buttonGroup}>

                            <button className={`${styles.screeningButton} ${styles.getTicketsButton}`}
                                    onClick={() => navigate(`/screenings/info/${screening.screening.id}`)}>Get tickets</button>
                            {/*<button className={styles.screeningButton}>View movie</button> */}
                            <button className={styles.screeningButton}
                                onClick={() => navigate(`/movie/${screening.movie.id}`)}>Schedule</button>

                        </div>

                    </div>
                ))}

            </div>

        </div>
    );
}

export default AllScreenings;
