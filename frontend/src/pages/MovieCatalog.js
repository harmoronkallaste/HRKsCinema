import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../style/MovieCatalog.module.css';

function MovieCatalog() {
    const navigate = useNavigate();
    const [movies, setMovies] = useState([]);

    useEffect(() => {
        // Fetch data from the backend API
        fetch('http://localhost:8080/movies/first10')
            .then(response => response.json())
            .then(data => setMovies(data))
            .catch(error => console.error('Error fetching data:', error));
    }, []);

    // Function to chunk the movies array into groups of 5
    const chunkArray = (arr, size) => {
        return Array.from({ length: Math.ceil(arr.length / size) }, (_, index) => {
            const start = index * size;
            return arr.slice(start, start + size);
        });
    };

    // Chunk the movies array into groups of 5
    const movieGroups = chunkArray(movies, 5);

    const goToScreenings = (movieId) => {
        navigate(`/movie/${movieId}`);
    };

    return (
        <div className={styles.movieCatalogWrapper}>
            <h2 className={styles.titleHeader}>Movie Catalog</h2>
            {movieGroups.map((group, index) => (
                <div key={index} className={styles.movieListRow}>
                    {group.map(movie => (
                        <div key={movie.id} className={styles.movie}>
                            <div className={styles.movieImage}>
                                <img src={movie.picUrl} alt={movie.movieName} />
                            </div>
                            <div className={styles.movieDetails}>
                                <div className={styles.movieName}>
                                    <h3><b>{movie.movieName}</b></h3>
                                </div>
                                <div className={styles.movieButtons}>
                                    <button className={styles.movieScreenings}
                                        onClick={() => goToScreenings(movie.id)}
                                    >
                                        View screenings</button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            ))}
        </div>
    );
}

export default MovieCatalog;
