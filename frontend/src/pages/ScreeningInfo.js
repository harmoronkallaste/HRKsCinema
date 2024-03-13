import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import styles from '../style/ScreeningInfo.module.css';
import moment from "moment/moment";

function TicketCounter({ title, price }) {
    const [count, setCount] = useState(0);

    const handleIncrement = () => {
        setCount(count + 1);
    };

    const handleDecrement = () => {
        if (count > 0) {
            setCount(count - 1);
        }
    };

    return (
        <div className={styles.ticketCounter}>
            <div className={styles.ticketDescription}>
                <span>{title}</span>
                <span className={styles.ticketPriceStyle}>{price} €</span>
            </div>
            <div className={styles.counterControls}>
                <button className={`${styles.counterControls} ${styles.decrementButton}`} onClick={handleDecrement} disabled={count === 0}>-</button>
                <span>{count}</span>
                <button className={`${styles.counterControls} ${styles.incrementButton}`} onClick={handleIncrement}>+</button>
            </div>
        </div>
    );
}

function Seat({ isOccupied, isVip, seatNumber, onHover }) {
    const seatClasses = `${styles.seat} ${isOccupied ? styles.occupied : ''} ${isVip ? styles.vip : ''}`;

    const handleMouseEnter = () => {
        if (onHover) {
            onHover(true);
        }
    };

    const handleMouseLeave = () => {
        if (onHover) {
            onHover(false);
        }
    };

    return (
        <div
            className={seatClasses}
            onMouseEnter={handleMouseEnter}
            onMouseLeave={handleMouseLeave}
            tabIndex="0"
        >
            {!isOccupied && (
                <span className={styles.seatNumber}>{seatNumber}</span>
            )}
        </div>
    );
}

function ScreeningInfo() {

    const [screeningInfo, setScreeningInfo] = useState(null);
    const [numberOfReservedSeats, setNumberOfReservedSeats] = useState(null);
    const {id} = useParams();
    const totalSeats = 100;

    useEffect(() => {
        fetch(`http://localhost:8080/screenings/info/${id}`)
            .then(response => response.json())
            .then(data => setScreeningInfo(data))
            .catch(error => console.error(error));

        fetch(`http://localhost:8080/reservations/howManyReserved/${id}`)
            .then(response => response.json())
            .then(data => setNumberOfReservedSeats(data))
            .catch(error => console.error(error));
    }, [id]);

    const formatScreeningTime = timestamp => {
        return moment(timestamp).format('LLL'); // Format the date and time as needed
    };

    if (!screeningInfo || numberOfReservedSeats === null) {
        return <div>Loading...</div>;
    }

    return (
        <div className={styles.screeningInfoBody}>
            <div className={styles.movieContainer}>
                <div className={styles.basicInformation}>
                    <h1 className={styles.movieTitle}> {screeningInfo.movie.movieName} </h1>
                    <h2 className={styles.dateTitle}>{formatScreeningTime(screeningInfo.screening.startTime)}</h2>
                    <h3 className={styles.roomTitle}> {screeningInfo.room.room_name} </h3>
                </div>
                <div className={styles.movieDetails}>
                    <div className={styles.moviePoster}>
                        <img src={screeningInfo.movie.picUrl}/>
                    </div>
                </div>
            </div>
            <SeatingArea occupiedSeatsCount={numberOfReservedSeats} totalSeats={totalSeats}
                         screeningInfo={screeningInfo}/>
        </div>
    );

}


function SeatingArea({occupiedSeatsCount, totalSeats, screeningInfo}) {

    const [seats, setSeats] = useState([]);

    useEffect(() => {
        let newSeats = Array(totalSeats).fill(false);
        let occupiedIndices = new Set();
        while (occupiedIndices.size < occupiedSeatsCount) {
            let randomIndex = Math.floor(Math.random() * totalSeats);
            if (!occupiedIndices.has(randomIndex)) {
                occupiedIndices.add(randomIndex);
                newSeats[randomIndex] = true;
            }
        }
        setSeats(newSeats);
    }, [occupiedSeatsCount, totalSeats]);

    const rows = 10;
    const cols = 10;

    return (
        <div className={styles.seatingContainer}>
            <div className={styles.ticketSelectionArea}>
                <TicketCounter title="Basic seat" price={screeningInfo.room.basicSeatPrice}/>
                <TicketCounter title="VIP seat" price={screeningInfo.room.vipSeatPrice}/>
            </div>
            <div className={styles.seatingInformation}>
                <h1 className={styles.seatingTitle}><b> Available
                    seats: </b> {totalSeats - occupiedSeatsCount} / {totalSeats}</h1>
            </div>
            <div className={styles.seatingArea}>
                {Array.from({length: rows}, (_, rowIndex) => {
                    return (
                        <div key={rowIndex} className={styles.row}>
                            {Array.from({length: cols}, (_, colIndex) => {
                                const seatId = rowIndex * cols + colIndex;
                                const seatNumber = colIndex + 1;
                                const isOccupied = seats[seatId];
                                const isVip = rowIndex === rows - 1;
                                return (
                                    <Seat
                                        key={seatId}
                                        isOccupied={isOccupied}
                                        isVip={isVip}
                                        seatNumber={seatNumber}
                                        onHover={isVip ? () => {
                                        } : undefined}
                                    />
                                );
                            })}
                            <div className={styles.rowNumber}> {rowIndex + 1 === 10 ? 'VIP' : rowIndex + 1} </div>
                        </div>
                    );
                })}
            </div>
            <div className={styles.screen}>Screen</div>
            <button className={styles.confirmTicketsButton}>
                Confirm tickets
            </button>
        </div>
    );

}

export default ScreeningInfo;
