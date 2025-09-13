import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { API_BASE_URL } from "./api";

function App() {
  const [message, setMessage] = useState("Press button to see message");

  console.log("API_BASE_URL =", API_BASE_URL);
  console.log(import.meta.env.VITE_API_URL)

  const fetchMessage = () => {
      fetch(`${API_BASE_URL}/random-message`)
        .then(res => {
          if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
          return res.json();
        })
        .then(data => {
          setMessage(`API1: ${API_BASE_URL}\nMassage: ${data.message}`);
        })
        .catch(err => {
          console.error(err);
          setMessage(`API1: ${API_BASE_URL}\nError API: ${err.message}`);
        });
    };

  return (
    <div style={{ padding: 20, fontFamily: "sans-serif" }}>
      <h1>React + Spring Boot</h1>
      <p>{message}</p>
      <button onClick={fetchMessage} style={{ padding: "10px 20px", marginTop: 10 }}>
        Отримати повідомлення
      </button>
    </div>
  );
}

export default App
