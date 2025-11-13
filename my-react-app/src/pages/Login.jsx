import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Login.css';

const Login = ({ onLoginSuccess }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await fetch('http://localhost:8081/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });

      if (!response.ok) {
        throw new Error('Invalid username or password');
      }

      const data = await response.json();
      
      // Store token and user info
      localStorage.setItem('token', data.token);
      localStorage.setItem('user', JSON.stringify(data));

      // Notify parent component
      onLoginSuccess(data);

      // Redirect to dashboard
      navigate('/dashboard');
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const testLogin = (user) => {
    setUsername(user.username);
    setPassword(user.password);
  };

  return (
    <div className="login-container">
      <div className="login-box">
        <h1>🔐 Dropshipping Management System</h1>
        <h2>Login</h2>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleLogin}>
          <div className="form-group">
            <label>Username</label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Enter username"
              required
            />
          </div>

          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter password"
              required
            />
          </div>

          <button type="submit" disabled={loading} className="login-btn">
            {loading ? 'Logging in...' : 'Login'}
          </button>
        </form>

        <div className="test-users">
          <h3>🧪 Test Users</h3>
          <button onClick={() => testLogin({ username: 'admin', password: 'admin123' })}>
            👤 Admin (admin123)
          </button>
          <button onClick={() => testLogin({ username: 'staff', password: 'staff123' })}>
            👥 Staff (staff123)
          </button>
          <button onClick={() => testLogin({ username: 'supplier', password: 'supplier123' })}>
            📦 Supplier (supplier123)
          </button>
          <button onClick={() => testLogin({ username: 'user', password: 'user123' })}>
            👤 User (user123)
          </button>
        </div>
      </div>
    </div>
  );
};

export default Login;
