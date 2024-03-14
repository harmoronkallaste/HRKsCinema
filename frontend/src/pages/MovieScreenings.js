/*import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Dropdown, Container, Row, Col, ListGroup } from 'react-bootstrap';
import moment from 'moment';
import 'bootstrap/dist/css/bootstrap.min.css'; // Make sure you import Bootstrap CSS
import styles from '../style/MovieScreenings.module.css';

function MovieScreenings() {
    const navigate = useNavigate();
    const [movieInfo, setMovieInfo] = useState(null);
    const [days, setDays] = useState([]);
    const [times, setTimes] = useState([]);
    const [selectedDays, setSelectedDays] = useState([]);
    const [selectedTimes, setSelectedTimes] = useState([]);
    const { id } = useParams();

    useEffect(() => {
        fetch(`http://localhost:8080/screenings/movie/${id}`)
            .then(response => response.json())
            .then(data => {
                setMovieInfo(data);

                const daySet = new Set(data.screenings.map(s => moment(s.screening.startTime).format('ddd')));
                const timeSet = new Set(data.screenings.map(s => moment(s.screening.startTime).format('HH:mm')));

                setDays(Array.from(daySet));
                setTimes(Array.from(timeSet));
            })
            .catch(error => console.error('Error fetching data:', error));
    }, [id]);

    const formatScreeningTime = timestamp => {
        return moment(timestamp).format('LLL');
    };

    const isChecked = (array, value) => array.includes(value);

    // Toggles day selection
    const toggleDaySelection = (day) => {
        setSelectedDays((prevSelectedDays) =>
            prevSelectedDays.includes(day)
                ? prevSelectedDays.filter((d) => d !== day)
                : [...prevSelectedDays, day]
        );
    };

    // Toggles time selection
    const toggleTimeSelection = (time) => {
        setSelectedTimes((prevSelectedTimes) =>
            prevSelectedTimes.includes(time)
                ? prevSelectedTimes.filter((t) => t !== time)
                : [...prevSelectedTimes, time]
        );
    };

    const goToScreening = screeningId => {
        navigate(`/screenings/info/${screeningId}`);
    };

    const handleDaySelection = value => {
        setSelectedDays(
            selectedDays.includes(value)
                ? selectedDays.filter(day => day !== value)
                : [...selectedDays, value]
        );
    };

    const handleTimeSelection = value => {
        setSelectedTimes(
            selectedTimes.includes(value)
                ? selectedTimes.filter(time => time !== value)
                : [...selectedTimes, value]
        );
    };

    const daysOfWeek = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];
    const screeningTimes = ["11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM", "8:00 PM", "9:00 PM"];


    const filteredScreenings = movieInfo?.screenings.filter(screening => {
        const day = moment(screening.screening.startTime).format('ddd');
        const time = moment(screening.screening.startTime).format('HH:mm');
        return (
            (selectedDays.length === 0 || selectedDays.includes(day)) &&
            (selectedTimes.length === 0 || selectedTimes.includes(time))
        );
    });

    if (!movieInfo) {
        return <div>Loading...</div>;
    }

    return (
        <div className={styles.movieScreeningsBody}>

            <div className={styles.movieInfoContainerWrapper}>
                <div className={styles.movieInfoContainerLeft}>
                    <h2 className={`${styles.containerHeader} ${styles.movieTitle}`}>{movieInfo.movie.movieName}</h2>
                    <div className={styles.movieDetails}>
                        <div className={styles.movieInfo}>
                            <p><b>Director(s):</b> {movieInfo.movie.directors}</p>
                            <p><b>Release Year:</b> {movieInfo.movie.releaseYear}</p>
                            <p><b>IMDb Rating:</b> {movieInfo.movie.rating}</p>
                            <p><b>Certificate:</b> {movieInfo.movie.certificate}</p>
                            <p><b>Runtime:</b> {movieInfo.movie.runTime}</p>
                        </div>
                        <div className={styles.moviePoster}>
                            <img src={movieInfo.movie.picUrl} alt={movieInfo.movie.movieName}/>
                        </div>
                    </div>
                </div>
                <div className={styles.movieInfoContainerRight}>
                    <div className={styles.filtersContainer}>
                        <Dropdown
                            title="Days"
                            items={days}
                            selectedItems={selectedDays}
                            onToggle={handleDaySelection}
                        />
                        <Dropdown
                            title="Times"
                            items={times}
                            selectedItems={selectedTimes}
                            onToggle={handleTimeSelection}
                        />
                    </div>
                    <Container>
                        <Dropdown>
                            <Dropdown.Toggle variant="warning">Filter</Dropdown.Toggle>
                            <Dropdown.Menu>
                                <Row style={{ minWidth: '50vw' }}>
                                    <Col>
                                        <ListGroup variant="flush">
                                            {daysOfWeek.map((day, index) => (
                                                <ListGroup.Item key={index} onClick={() => toggleDaySelection(day)}>
                                                    <input
                                                        type="checkbox"
                                                        checked={isChecked(selectedDays, day)}
                                                        onChange={() => {}}
                                                    />{' '}
                                                    {day}
                                                </ListGroup.Item>
                                            ))}
                                        </ListGroup>
                                    </Col>
                                    <Col>
                                        <ListGroup>
                                            {screeningTimes.map((time, index) => (
                                                <ListGroup.Item key={index} onClick={() => toggleTimeSelection(time)}>
                                                    <input
                                                        type="checkbox"
                                                        checked={isChecked(selectedTimes, time)}
                                                        onChange={() => {}}
                                                    />{' '}
                                                    {time}
                                                </ListGroup.Item>
                                            ))}
                                        </ListGroup>
                                    </Col>
                                </Row>
                            </Dropdown.Menu>
                        </Dropdown>
                    </Container>
                    <div className={styles.screeningGrid}>
                        {filteredScreenings.map(screening => (
                            <div key={screening.screening.id} className={styles.screeningItem}>
                                <button
                                    className={styles.directToScreeningButton}
                                    onClick={() => goToScreening(screening.screening.id)}
                                >
                                    {formatScreeningTime(screening.screening.startTime)}
                                </button>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default MovieScreenings;
*/

import React, { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import moment from 'moment';
import styles from '../style/MovieScreenings.module.css';

function useOutsideAlerter(ref, setter) {
    useEffect(() => {
        function handleClickOutside(event) {
            if (ref.current && !ref.current.contains(event.target)) {
                setter(false);
            }
        }
        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [ref, setter]);
}

function Dropdown({ title, items, selectedItems, onToggle }) {
    const [isOpen, setIsOpen] = useState(false);
    const wrapperRef = useRef(null);
    useOutsideAlerter(wrapperRef, setIsOpen);

    return (
        <div className={styles.dropdown} ref={wrapperRef}>
            <button className={styles.dropdownButton} onClick={() => setIsOpen(!isOpen)}>
                {title}
            </button>
            {isOpen && (
                <div className={styles.dropdownContent}>
                    {items.map(item => (
                        <label key={item} className={styles.dropdownLabel}>
                            <input
                                type="checkbox"
                                value={item}
                                checked={selectedItems.includes(item)}
                                onChange={(e) => onToggle(e.target.value)}
                            />
                            {item}
                        </label>
                    ))}
                </div>
            )}
        </div>
    );
}

function MovieScreenings() {
    const navigate = useNavigate();
    const [movieInfo, setMovieInfo] = useState(null);
    const [selectedDays, setSelectedDays] = useState([]);
    const [selectedTimes, setSelectedTimes] = useState([]);
    const { id } = useParams();

    useEffect(() => {
        fetch(`http://localhost:8080/screenings/movie/${id}`)
            .then(response => response.json())
            .then(data => {
                setMovieInfo(data);
            })
            .catch(error => console.error('Error fetching data:', error));
    }, [id]);

    const formatScreeningTime = timestamp => {
        return moment(timestamp).format('LLL');
    };

    const goToScreening = screeningId => {
        navigate(`/screenings/info/${screeningId}`);
    };

    const filteredScreenings = movieInfo?.screenings.filter(screening => {
        const day = moment(screening.screening.startTime).format('ddd');
        const time = moment(screening.screening.startTime).format('HH:mm');
        return (
            (selectedDays.length === 0 || selectedDays.includes(day)) &&
            (selectedTimes.length === 0 || selectedTimes.includes(time))
        );
    });

    if (!movieInfo) {
        return <div>Loading...</div>;
    }

    return (
        <div className={styles.movieScreeningsBody}>
            <div className={styles.movieInfoContainerWrapper}>
                <div className={styles.movieInfoContainerLeft}>
                    <h2 className={`${styles.containerHeader} ${styles.movieTitle}`}>{movieInfo.movie.movieName}</h2>
                    <div className={styles.movieDetails}>
                        <div className={styles.movieInfo}>
                            <p><b>Director(s):</b> {movieInfo.movie.directors}</p>
                            <p><b>Release Year:</b> {movieInfo.movie.releaseYear}</p>
                            <p><b>IMDb Rating:</b> {movieInfo.movie.rating}</p>
                            <p><b>Certificate:</b> {movieInfo.movie.certificate}</p>
                            <p><b>Runtime:</b> {movieInfo.movie.runTime}</p>
                        </div>
                        <div className={styles.moviePoster}>
                            <img src={movieInfo.movie.picUrl} alt={movieInfo.movie.movieName}/>
                        </div>
                    </div>
                </div>
                <div className={styles.movieInfoContainerRight}>
                    <div className={styles.screeningGrid}>
                        {filteredScreenings.map(screening => (
                            <div key={screening.screening.id} className={styles.screeningItem}>
                                <button
                                    className={styles.directToScreeningButton}
                                    onClick={() => goToScreening(screening.screening.id)}
                                >
                                    {formatScreeningTime(screening.screening.startTime)}
                                </button>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default MovieScreenings;
