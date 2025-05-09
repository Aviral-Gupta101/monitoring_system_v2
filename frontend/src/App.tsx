import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import HomePage from './pages/HomePage';
import { Toaster } from './components/ui/sonner';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage';
import { SERVER_ADDRESS } from './lib/global';
import axios from 'axios';


function App() {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  
  // Check if user is authenticated on component mount
  useEffect(() => {

    const token = localStorage.getItem('authToken');

    if (token) {

      axios.get(SERVER_ADDRESS + "auth/me", {
        headers: { Authorization: `Bearer ${localStorage.getItem("authToken")}` }
      }).then(() => {
        setIsAuthenticated(true)
      }).catch((error) => {

        console.log("Token expired !!")
        console.log(error)
      });
    }
  }, []);

  const handleLogin = (token: string): void => {
    localStorage.setItem('authToken', token);
    setIsAuthenticated(true);
  };

  const handleLogout = (): void => {
    localStorage.removeItem('authToken');
    setIsAuthenticated(false);
  };

  return (
    <Router>
      <Routes>
        <Route 
          path="/" 
          element={isAuthenticated ? <Navigate to="/dashboard" /> : <LoginPage onLogin={handleLogin} />} 
        />
        <Route 
          path="/dashboard" 
          element={isAuthenticated ? <HomePage onLogout={handleLogout} /> : <Navigate to="/" />} 
          // element={<HomePage onLogout={handleLogout} /> } 
        />
        <Route 
          path="/signup" 
          element={isAuthenticated ? <Navigate to="/dashboard" /> : <SignupPage/>} 
        />
      </Routes>
      <Toaster />
    </Router>
  );
}

export default App;