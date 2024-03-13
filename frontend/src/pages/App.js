import React from 'react';
import '../style/App.css';
import {BrowserRouter as Router, Routes, Route, useNavigate} from 'react-router-dom';
import MovieScreenings from "./MovieScreenings";
import GuestPage from './GuestPage';
import LoginPage from './LoginPage';
import MovieCatalog from "./MovieCatalog";
import ScreeningInfo from "./ScreeningInfo";
import logoHrk from '../other/logohrk.png'

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<LandingPage />} />
                <Route path="/movie/:id" element={<MovieScreenings />} />
                <Route path="/guest" element={<GuestPage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/catalog" element={<MovieCatalog />} />
                <Route path="/screenings/info/:id" element={<ScreeningInfo />} />
            </Routes>
        </Router>
    );
}

function LandingPage() {
    const navigate = useNavigate();

    return (
        <div className="App">
            <header className="App-header">
                <div className="centered-content">
                    <img src={logoHrk} alt="HRK Cinema Logo" className="cinemaLogo"/>
                    <h1>Welcome to HRK's Cinema</h1>
                </div>
            </header>
            <main className="landing-page">
                <div className="login-option">
                    <button className="movie-catalog-option" onClick={() => navigate('/catalog')}> Browse movies
                    </button>
                    <p className="description"> This will allow you to see all movies that are being screened. </p>
                </div>
                <div className="login-option">
                    <button className="google-login" onClick={() => navigate('/login')}> Login using Google</button>
                    <p className="description"> This will allow you to access your booking history and
                        preferences. </p>
                </div>
                <div className="login-option">
                    <button className="guest-login" onClick={() => navigate('/guest')}> Continue as guest</button>
                    <p className="description"> No login required. </p>
                </div>
            </main>
        </div>
    );
}

export default App;
