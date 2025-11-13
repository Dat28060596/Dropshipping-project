import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Analytics.css';

const Analytics = () => {
  const navigate = useNavigate();
  const [selectedMetric, setSelectedMetric] = useState('sales');

  const metrics = {
    sales: {
      title: '📊 Sales Reports',
      data: [
        { month: 'January', sales: 45000, orders: 234 },
        { month: 'February', sales: 52000, orders: 267 },
        { month: 'March', sales: 48000, orders: 245 },
        { month: 'April', sales: 61000, orders: 312 },
        { month: 'May', sales: 55000, orders: 289 },
        { month: 'June', sales: 67000, orders: 345 }
      ]
    },
    inventory: {
      title: '📦 Inventory Trends',
      data: [
        { product: 'Electronics', stock: 450, sold: 234 },
        { product: 'Clothing', stock: 890, sold: 567 },
        { product: 'Home & Garden', stock: 320, sold: 178 },
        { product: 'Sports', stock: 210, sold: 145 },
        { product: 'Books', stock: 560, sold: 432 }
      ]
    },
    customer: {
      title: '👥 Customer Behavior Insights',
      data: [
        { segment: 'New Customers', count: 234, avgSpent: 156.80 },
        { segment: 'Returning Customers', count: 567, avgSpent: 234.50 },
        { segment: 'VIP Customers', count: 89, avgSpent: 1245.60 },
        { segment: 'At Risk', count: 45, avgSpent: 98.20 }
      ]
    },
    profitability: {
      title: '💰 Profitability Tracking',
      data: [
        { category: 'Electronics', revenue: 125000, cost: 75000, profit: 50000 },
        { category: 'Clothing', revenue: 89000, cost: 45000, profit: 44000 },
        { category: 'Home & Garden', revenue: 67000, cost: 32000, profit: 35000 },
        { category: 'Sports', revenue: 45000, cost: 20000, profit: 25000 },
        { category: 'Books', revenue: 34000, cost: 18000, profit: 16000 }
      ]
    }
  };

  const currentMetric = metrics[selectedMetric];

  return (
    <div className="analytics-container">
      <header className="page-header">
        <button className="back-btn" onClick={() => navigate('/dashboard')}>
          ← Back to Dashboard
        </button>
        <h1>📊 Analytics & Reporting</h1>
      </header>

      <div className="analytics-content">
        <nav className="metrics-nav">
          <button
            className={`metric-btn ${selectedMetric === 'sales' ? 'active' : ''}`}
            onClick={() => setSelectedMetric('sales')}
          >
            📈 Sales Reports
          </button>
          <button
            className={`metric-btn ${selectedMetric === 'inventory' ? 'active' : ''}`}
            onClick={() => setSelectedMetric('inventory')}
          >
            📦 Inventory Trends
          </button>
          <button
            className={`metric-btn ${selectedMetric === 'customer' ? 'active' : ''}`}
            onClick={() => setSelectedMetric('customer')}
          >
            👥 Customer Insights
          </button>
          <button
            className={`metric-btn ${selectedMetric === 'profitability' ? 'active' : ''}`}
            onClick={() => setSelectedMetric('profitability')}
          >
            💰 Profitability
          </button>
        </nav>

        <section className="metrics-display">
          <h2>{currentMetric.title}</h2>

          <div className="table-wrapper">
            <table className="metrics-table">
              <thead>
                <tr>
                  {selectedMetric === 'sales' && (
                    <>
                      <th>Month</th>
                      <th>Sales Revenue</th>
                      <th>Number of Orders</th>
                    </>
                  )}
                  {selectedMetric === 'inventory' && (
                    <>
                      <th>Product</th>
                      <th>Stock Available</th>
                      <th>Units Sold</th>
                    </>
                  )}
                  {selectedMetric === 'customer' && (
                    <>
                      <th>Segment</th>
                      <th>Customer Count</th>
                      <th>Average Spent</th>
                    </>
                  )}
                  {selectedMetric === 'profitability' && (
                    <>
                      <th>Category</th>
                      <th>Revenue</th>
                      <th>Cost</th>
                      <th>Profit</th>
                      <th>Margin %</th>
                    </>
                  )}
                </tr>
              </thead>
              <tbody>
                {currentMetric.data.map((row, idx) => (
                  <tr key={idx}>
                    {selectedMetric === 'sales' && (
                      <>
                        <td>{row.month}</td>
                        <td className="amount">${row.sales.toLocaleString()}</td>
                        <td>{row.orders}</td>
                      </>
                    )}
                    {selectedMetric === 'inventory' && (
                      <>
                        <td>{row.product}</td>
                        <td>{row.stock}</td>
                        <td>{row.sold}</td>
                      </>
                    )}
                    {selectedMetric === 'customer' && (
                      <>
                        <td>{row.segment}</td>
                        <td>{row.count}</td>
                        <td className="amount">${row.avgSpent.toFixed(2)}</td>
                      </>
                    )}
                    {selectedMetric === 'profitability' && (
                      <>
                        <td>{row.category}</td>
                        <td className="amount">${row.revenue.toLocaleString()}</td>
                        <td className="amount">${row.cost.toLocaleString()}</td>
                        <td className="amount profit">${row.profit.toLocaleString()}</td>
                        <td>{((row.profit / row.revenue) * 100).toFixed(1)}%</td>
                      </>
                    )}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="metrics-summary">
            <h3>Summary</h3>
            {selectedMetric === 'sales' && (
              <div className="summary-stats">
                <div className="summary-item">
                  <span>Total Revenue:</span>
                  <strong>${currentMetric.data.reduce((sum, d) => sum + d.sales, 0).toLocaleString()}</strong>
                </div>
                <div className="summary-item">
                  <span>Total Orders:</span>
                  <strong>{currentMetric.data.reduce((sum, d) => sum + d.orders, 0)}</strong>
                </div>
                <div className="summary-item">
                  <span>Average per Month:</span>
                  <strong>${Math.round(currentMetric.data.reduce((sum, d) => sum + d.sales, 0) / currentMetric.data.length).toLocaleString()}</strong>
                </div>
              </div>
            )}
            {selectedMetric === 'inventory' && (
              <div className="summary-stats">
                <div className="summary-item">
                  <span>Total Stock:</span>
                  <strong>{currentMetric.data.reduce((sum, d) => sum + d.stock, 0)}</strong>
                </div>
                <div className="summary-item">
                  <span>Total Sold:</span>
                  <strong>{currentMetric.data.reduce((sum, d) => sum + d.sold, 0)}</strong>
                </div>
              </div>
            )}
            {selectedMetric === 'profitability' && (
              <div className="summary-stats">
                <div className="summary-item">
                  <span>Total Revenue:</span>
                  <strong>${currentMetric.data.reduce((sum, d) => sum + d.revenue, 0).toLocaleString()}</strong>
                </div>
                <div className="summary-item">
                  <span>Total Profit:</span>
                  <strong>${currentMetric.data.reduce((sum, d) => sum + d.profit, 0).toLocaleString()}</strong>
                </div>
              </div>
            )}
          </div>
        </section>
      </div>
    </div>
  );
};

export default Analytics;
