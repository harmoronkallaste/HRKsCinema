import React, { useState, useEffect } from 'react';
import '../style/Guest.css';
import moment from 'moment/moment'; // Import moment.js library
import { Link, useParams } from 'react-router-dom'; // Import Link from react-router-dom

function GuestPage() {
    const [movieInfo, setMovieInfo] = useState(null);
    const { id } = useParams(); // Accessing path parameters using useParams

    useEffect(() => {
        fetch(`http://localhost:8080/screenings/movie/${id}`) // Use the correct backend URL
            .then(response => response.json())
            .then(data => {
                setMovieInfo(data);
            })
            .catch(error => console.error(error));
    }, [id]); // Include 'id' as a dependency to re-fetch data when 'id' changes

    const formatScreeningTime = (timestampInMilliseconds) => {
        const dateObject = new Date(timestampInMilliseconds);
        const day = moment(dateObject).format('Do'); // Get the day with ordinal suffix (e.g., 1st, 2nd, 3rd)
        const monthYear = moment(dateObject).format('MMMM YYYY'); // Get the month and year
        const time = moment(dateObject).format('HH:mm'); // Get the time
        return `${day} of ${monthYear}, ${time}`; // Concatenate the day, month/year, and time
    };

    return (
        <div className="guest-page">
            <h1>Welcome as Guest</h1>
            {movieInfo && movieInfo.movie && (
                <div className="movie-container">
                    <h3>{movieInfo.movie.movieName}</h3>
                    <p>Rating: {movieInfo.movie.rating}</p>
                    <p>Release Year: {movieInfo.movie.releaseYear}</p>
                    <p>Runtime: {movieInfo.movie.runTime}</p>
                    <div className="screening-info">
                        <h4>Screening Times:</h4>
                        <ul>
                            {movieInfo.screenings && movieInfo.screenings.length > 0 ? (
                                movieInfo.screenings.map(screening => (
                                    <li key={screening.screening.id}>
                                        <Link to={`/screenings/info/${screening.screening.id}`}>
                                            <button className="screening-button">
                                                {formatScreeningTime(screening.screening.startTime)}
                                            </button>
                                        </Link>
                                    </li>
                                ))
                            ) : (
                                <p>No screenings available for this movie.</p>
                            )}
                        </ul>
                    </div>
                </div>
            )}
        </div>
    );
}

export default GuestPage;
