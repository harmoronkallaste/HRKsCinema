import axios from 'axios';

const API_URL = 'http://localhost:8080/api'; // Proxy configured in setupProxy.js

// Example function to fetch data from backend
export const fetchData = async () => {
    try {
        const response = await axios.get(`${API_URL}/your-endpoint`);
        return response.data;
    } catch (error) {
        console.error('Error fetching data:', error);
        throw error;
    }
};