import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import styles from '../style/ScreeningInfo.module.css';
import moment from "moment/moment";

function TicketCounter({ title, price, selectedSeats, onIncrement, onDecrement }) {
    return (
        <div className={styles.ticketCounter}>
            <div className={styles.ticketDescription}>
                <span>{title}</span>
                <span className={styles.ticketPriceStyle}>{price} €</span>
            </div>
            <div className={styles.counterControls}>
                <button className={`${styles.counterButton} ${styles.decrementButton}`} onClick={onDecrement} disabled={selectedSeats === 0}>-</button>
                <span>{selectedSeats}</span>
                <button className={`${styles.counterButton} ${styles.incrementButton}`} onClick={onIncrement}>+</button>
            </div>
        </div>
    );
}

function Seat({ isOccupied, isVip, seatNumber, onSelect, isSelected }) {
    const seatClasses = `${styles.seat} ${isOccupied ? styles.occupied : ''} ${isVip ? styles.vip : ''} ${isSelected ? styles.selected : ''}`;

    return (
        <div
            className={seatClasses}
            onClick={() => onSelect(seatNumber, isVip)}
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
        async function fetchData() {
            try {
                const infoResponse = await fetch(`http://localhost:8080/screenings/info/${id}`);
                const infoData = await infoResponse.json();
                setScreeningInfo(infoData);

            } catch (error) {
                console.error('Fetching error:', error);
            }
        }

        fetchData();
    }, [id]);


    const formatScreeningTime = timestamp => {
        return moment(timestamp).format('dddd HH:mm, MMM Do YYYY');
    };

    if (!screeningInfo) {
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
            <SeatingArea
                          totalSeats={100} // Assuming fixed number, adjust as necessary
                          screeningInfo={screeningInfo}/>
        </div>
    );
}

function SeatingArea({occupiedSeatsCount, totalSeats, screeningInfo}) {
    const [seats, setSeats] = useState([]);
    const [selectedSeats, setSelectedSeats] = useState(new Set());
    const [selectedVipSeats, setSelectedVipSeats] = useState(new Set());

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

    const handleSelectSeat = (seatNumber, isVip) => {
        if (isVip) {
            setSelectedVipSeats(prev => new Set(prev.has(seatNumber) ? [...prev].filter(x => x !== seatNumber) : [...prev, seatNumber]));
        } else {
            setSelectedSeats(prev => new Set(prev.has(seatNumber) ? [...prev].filter(x => x !== seatNumber) : [...prev, seatNumber]));
        }
    };

    const rows = 10;
    const cols = 10;

    const handleIncrement = (isVip) => {
        // Find the first available seat that is not occupied and not selected
        for (let i = 0; i < seats.length; i++) {
            const seatIsVip = i >= (rows - 1) * cols; // Assumes last row is VIP
            if (!seats[i] && seatIsVip === isVip && !(selectedSeats.has(i) || selectedVipSeats.has(i))) {
                if (isVip) {
                    setSelectedVipSeats(new Set(selectedVipSeats).add(i));
                } else {
                    setSelectedSeats(new Set(selectedSeats).add(i));
                }
                break;
            }
        }
    };

    const handleDecrement = (isVip) => {
        // Deselect the last selected seat of the appropriate type
        if (isVip) {
            if (selectedVipSeats.size > 0) {
                const updatedVipSeats = new Set(selectedVipSeats);
                const lastSelected = Array.from(updatedVipSeats).pop(); // Get the last element
                updatedVipSeats.delete(lastSelected);
                setSelectedVipSeats(updatedVipSeats);
            }
        } else {
            if (selectedSeats.size > 0) {
                const updatedSeats = new Set(selectedSeats);
                const lastSelected = Array.from(updatedSeats).pop(); // Get the last element
                updatedSeats.delete(lastSelected);
                setSelectedSeats(updatedSeats);
            }
        }
    };

    return (
        <div className={styles.seatingContainer}>
            <div className={styles.ticketSelectionArea}>
                <TicketCounter title="Basic seat" price={screeningInfo.room.basicSeatPrice}
                               selectedSeats={selectedSeats.size}
                               onIncrement={() => handleIncrement(false)}
                               onDecrement={() => handleDecrement(false)}/>
                <TicketCounter title="VIP seat" price={screeningInfo.room.vipSeatPrice}
                               selectedSeats={selectedVipSeats.size}
                               onIncrement={() => handleIncrement(true)}
                               onDecrement={() => handleDecrement(true)}/>
            </div>
            <div className={styles.seatingInformation}>
                <h1 className={styles.seatingTitle}><b> Available
                    seats: </b> {totalSeats - occupiedSeatsCount} / {totalSeats}</h1>
            </div>
            <div className={styles.seatingArea}>
                {Array.from({length: rows}, (_, rowIndex) => (
                    <div key={rowIndex} className={styles.row}>
                        {Array.from({length: cols}, (_, colIndex) => {
                            const seatId = rowIndex * cols + colIndex;
                            const seatNumber = colIndex + 1;
                            const isOccupied = seats[seatId];
                            const isVip = rowIndex === rows - 1;
                            return (
                                <Seat
                                    key={seatId}
                                    isVip={isVip}
                                    seatNumber={seatNumber}
                                    onSelect={() => handleSelectSeat(seatId, isVip)}
                                    isSelected={selectedSeats.has(seatId) || selectedVipSeats.has(seatId)}
                                />
                            );
                        })}
                        <div className={styles.rowNumber}>{rowIndex + 1 === 10 ? 'VIP' : rowIndex + 1}</div>
                    </div>
                ))}
            </div>
            <div className={styles.screen}>Screen</div>
            <button className={styles.confirmTicketsButton}>Confirm tickets</button>
        </div>
    );
}

export default ScreeningInfo;
