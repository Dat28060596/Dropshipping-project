import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Dashboard.css';

const Dashboard = ({ user }) => {
  const navigate = useNavigate();

  if (!user) {
    return <div className="loading">Loading...</div>;
  }

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    navigate('/login');
  };

  const features = [
    {
      id: 'analytics',
      title: '📊 Analytics & Reporting',
      description: 'Sales reports, inventory trends, customer behavior insights, and profitability tracking',
      icon: '📈',
      path: '/analytics',
      roles: ['ADMIN', 'STAFF']
    },
    {
      id: 'rbac',
      title: '🔐 User Roles & Permissions',
      description: 'Role-based access for admins, staff, and suppliers with different access levels',
      icon: '👥',
      path: '/rbac',
      roles: ['ADMIN']
    },
    {
      id: 'payment',
      title: '💳 Payment Processing',
      description: 'Integration with multiple payment gateways with secure transaction handling',
      icon: '💰',
      path: '/payment',
      roles: ['ADMIN', 'STAFF']
    }
  ];

  const userRoles = user.roles || [];

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <div className="header-left">
          <h1>🏪 Dropshipping Management System</h1>
        </div>
        <div className="header-right">
          <span className="user-info">
            👤 {user.fullName} ({user.roles.join(', ')})
          </span>
          <button onClick={handleLogout} className="logout-btn">
            Logout
          </button>
        </div>
      </header>

      <main className="dashboard-main">
        <div className="welcome-section">
          <h2>Welcome, {user.fullName}! 👋</h2>
          <p>Here are the available features for your role: <strong>{userRoles.join(', ')}</strong></p>
        </div>

        <div className="features-grid">
          {features.map((feature) => {
            const hasAccess = feature.roles.some(role => userRoles.includes(role));
            return (
              <div
                key={feature.id}
                className={`feature-card ${!hasAccess ? 'disabled' : ''}`}
                onClick={() => hasAccess && navigate(feature.path)}
              >
                <div className="feature-icon">{feature.icon}</div>
                <h3>{feature.title}</h3>
                <p>{feature.description}</p>
                {!hasAccess && (
                  <div className="access-denied">
                    🔒 Access denied for your role
                  </div>
                )}
              </div>
            );
          })}
        </div>

        <section className="user-stats">
          <h3>📊 Quick Stats</h3>
          <div className="stats-grid">
            <div className="stat-card">
              <span className="stat-value">1,234</span>
              <span className="stat-label">Total Orders</span>
            </div>
            <div className="stat-card">
              <span className="stat-value">$45,678</span>
              <span className="stat-label">Total Revenue</span>
            </div>
            <div className="stat-card">
              <span className="stat-value">892</span>
              <span className="stat-label">Active Users</span>
            </div>
            <div className="stat-card">
              <span className="stat-value">98.5%</span>
              <span className="stat-label">Success Rate</span>
            </div>
          </div>
        </section>
      </main>
    </div>
  );
};

export default Dashboard;
