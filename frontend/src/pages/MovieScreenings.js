import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import moment from 'moment';
import styles from '../style/MovieScreenings.module.css';

function MovieScreenings() {
    const navigate = useNavigate();
    const [movieInfo, setMovieInfo] = useState(null);
    const { id } = useParams(); // This will get the movieId from the URL

    useEffect(() => {
        fetch(`http://localhost:8080/screenings/movie/${id}`)
            .then(response => response.json())
            .then(data => {
                setMovieInfo(data);
            })
            .catch(error => console.error('Error fetching data:', error));
    }, [id]);

    const formatScreeningTime = timestamp => {
        return moment(timestamp).format('LLL'); // Format the date and time as needed
    };

    const goToScreening = (screeningId) => {
        navigate(`/screenings/info/${screeningId}`)
    };

    if (!movieInfo) {
        return <div>Loading...</div>; // Display loading message until data is fetched
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
                            <img src={movieInfo.movie.picUrl}/>
                        </div>
                    </div>
                </div>
                <div className={styles.movieInfoContainerRight}>
                    {/*<h3 className={`${styles.containerHeader} ${styles.screeningsTitle}`}>Choose a screening:</h3>*/}
                    <div className={styles.screeningGrid}> {/* Wrapper for screening times */}
                        {movieInfo.screenings && movieInfo.screenings.map(screening => (
                            <div key={screening.screening.id} className={styles.screeningItem}>
                                <button className={styles.directToScreeningButton}
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
