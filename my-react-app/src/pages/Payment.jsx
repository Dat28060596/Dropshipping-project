import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Payment.css';

const Payment = () => {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('process');
  const [transactions, setTransactions] = useState([
    {
      id: 'TXN001',
      date: '2025-11-14',
      amount: 234.50,
      gateway: 'Stripe',
      status: 'Completed',
      customer: 'John Doe',
      method: 'Credit Card'
    },
    {
      id: 'TXN002',
      date: '2025-11-13',
      amount: 567.89,
      gateway: 'PayPal',
      status: 'Completed',
      customer: 'Jane Smith',
      method: 'PayPal'
    },
    {
      id: 'TXN003',
      date: '2025-11-12',
      amount: 123.45,
      gateway: 'Stripe',
      status: 'Failed',
      customer: 'Bob Johnson',
      method: 'Debit Card'
    },
    {
      id: 'TXN004',
      date: '2025-11-11',
      amount: 890.00,
      gateway: 'Square',
      status: 'Pending',
      customer: 'Alice Williams',
      method: 'Credit Card'
    }
  ]);

  const [formData, setFormData] = useState({
    customerName: '',
    amount: '',
    gateway: 'stripe',
    cardNumber: '',
    expiryDate: '',
    cvv: ''
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleProcessPayment = (e) => {
    e.preventDefault();

    if (!formData.customerName || !formData.amount) {
      alert('Please fill in all required fields');
      return;
    }

    const newTransaction = {
      id: `TXN${String(transactions.length + 1).padStart(3, '0')}`,
      date: new Date().toISOString().split('T')[0],
      amount: parseFloat(formData.amount),
      gateway: formData.gateway.charAt(0).toUpperCase() + formData.gateway.slice(1),
      status: 'Completed',
      customer: formData.customerName,
      method: 'Credit Card'
    };

    setTransactions([newTransaction, ...transactions]);
    setFormData({
      customerName: '',
      amount: '',
      gateway: 'stripe',
      cardNumber: '',
      expiryDate: '',
      cvv: ''
    });
    alert('Payment processed successfully!');
  };

  const getGatewayIcon = (gateway) => {
    const icons = {
      'Stripe': '🔵',
      'PayPal': '⚫',
      'Square': '⬜',
      'Google Pay': '🔵'
    };
    return icons[gateway] || '💳';
  };

  const getStatusColor = (status) => {
    const colors = {
      'Completed': 'status-completed',
      'Pending': 'status-pending',
      'Failed': 'status-failed'
    };
    return colors[status] || '';
  };

  return (
    <div className="payment-container">
      <header className="page-header">
        <button className="back-btn" onClick={() => navigate('/dashboard')}>
          ← Back to Dashboard
        </button>
        <h1>💳 Payment Processing</h1>
      </header>

      <div className="payment-content">
        <nav className="payment-tabs">
          <button
            className={`tab-btn ${activeTab === 'process' ? 'active' : ''}`}
            onClick={() => setActiveTab('process')}
          >
            💳 Process Payment
          </button>
          <button
            className={`tab-btn ${activeTab === 'history' ? 'active' : ''}`}
            onClick={() => setActiveTab('history')}
          >
            📋 Transaction History
          </button>
          <button
            className={`tab-btn ${activeTab === 'gateways' ? 'active' : ''}`}
            onClick={() => setActiveTab('gateways')}
          >
            🔌 Payment Gateways
          </button>
        </nav>

        {activeTab === 'process' && (
          <section className="process-section">
            <h2>Process New Payment</h2>
            <form className="payment-form" onSubmit={handleProcessPayment}>
              <div className="form-group">
                <label>Customer Name *</label>
                <input
                  type="text"
                  name="customerName"
                  value={formData.customerName}
                  onChange={handleInputChange}
                  placeholder="Enter customer name"
                  required
                />
              </div>

              <div className="form-group">
                <label>Amount ($) *</label>
                <input
                  type="number"
                  name="amount"
                  value={formData.amount}
                  onChange={handleInputChange}
                  placeholder="Enter amount"
                  step="0.01"
                  required
                />
              </div>

              <div className="form-group">
                <label>Payment Gateway *</label>
                <select
                  name="gateway"
                  value={formData.gateway}
                  onChange={handleInputChange}
                >
                  <option value="stripe">Stripe</option>
                  <option value="paypal">PayPal</option>
                  <option value="square">Square</option>
                  <option value="googlepay">Google Pay</option>
                </select>
              </div>

              <div className="form-group">
                <label>Card Number</label>
                <input
                  type="text"
                  name="cardNumber"
                  value={formData.cardNumber}
                  onChange={handleInputChange}
                  placeholder="1234 5678 9012 3456"
                />
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>Expiry Date</label>
                  <input
                    type="text"
                    name="expiryDate"
                    value={formData.expiryDate}
                    onChange={handleInputChange}
                    placeholder="MM/YY"
                  />
                </div>
                <div className="form-group">
                  <label>CVV</label>
                  <input
                    type="text"
                    name="cvv"
                    value={formData.cvv}
                    onChange={handleInputChange}
                    placeholder="123"
                    maxLength="4"
                  />
                </div>
              </div>

              <button type="submit" className="btn-process">
                Process Payment
              </button>
            </form>
          </section>
        )}

        {activeTab === 'history' && (
          <section className="history-section">
            <h2>Transaction History</h2>
            <div className="table-wrapper">
              <table className="transactions-table">
                <thead>
                  <tr>
                    <th>Transaction ID</th>
                    <th>Date</th>
                    <th>Customer</th>
                    <th>Amount</th>
                    <th>Gateway</th>
                    <th>Method</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  {transactions.map(txn => (
                    <tr key={txn.id}>
                      <td className="txn-id">{txn.id}</td>
                      <td>{txn.date}</td>
                      <td>{txn.customer}</td>
                      <td className="amount">${txn.amount.toFixed(2)}</td>
                      <td>{getGatewayIcon(txn.gateway)} {txn.gateway}</td>
                      <td>{txn.method}</td>
                      <td>
                        <span className={`status-badge ${getStatusColor(txn.status)}`}>
                          {txn.status}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            <div className="transaction-stats">
              <div className="stat">
                <span>Total Transactions:</span>
                <strong>{transactions.length}</strong>
              </div>
              <div className="stat">
                <span>Total Revenue:</span>
                <strong>${transactions.reduce((sum, t) => sum + t.amount, 0).toFixed(2)}</strong>
              </div>
              <div className="stat">
                <span>Success Rate:</span>
                <strong>{Math.round((transactions.filter(t => t.status === 'Completed').length / transactions.length) * 100)}%</strong>
              </div>
            </div>
          </section>
        )}

        {activeTab === 'gateways' && (
          <section className="gateways-section">
            <h2>Integrated Payment Gateways</h2>
            <div className="gateways-grid">
              <div className="gateway-card">
                <div className="gateway-icon">🔵</div>
                <h3>Stripe</h3>
                <p>Secure credit card processing with PCI DSS compliance</p>
                <ul className="features">
                  <li>✅ Credit/Debit Cards</li>
                  <li>✅ Apple Pay & Google Pay</li>
                  <li>✅ Global Currency Support</li>
                  <li>✅ Fraud Detection</li>
                </ul>
                <span className="gateway-status connected">Connected</span>
              </div>

              <div className="gateway-card">
                <div className="gateway-icon">⚫</div>
                <h3>PayPal</h3>
                <p>Digital payment platform with buyer protection</p>
                <ul className="features">
                  <li>✅ PayPal Wallet</li>
                  <li>✅ Credit Cards</li>
                  <li>✅ Bank Transfers</li>
                  <li>✅ Installment Plans</li>
                </ul>
                <span className="gateway-status connected">Connected</span>
              </div>

              <div className="gateway-card">
                <div className="gateway-icon">⬜</div>
                <h3>Square</h3>
                <p>All-in-one payment solution for businesses</p>
                <ul className="features">
                  <li>✅ Card Payments</li>
                  <li>✅ Digital Wallets</li>
                  <li>✅ ACH Transfers</li>
                  <li>✅ Point of Sale</li>
                </ul>
                <span className="gateway-status connected">Connected</span>
              </div>

              <div className="gateway-card">
                <div className="gateway-icon">🔵</div>
                <h3>Google Pay</h3>
                <p>Fast and secure payment with tokenized cards</p>
                <ul className="features">
                  <li>✅ Mobile Payments</li>
                  <li>✅ Web Payments</li>
                  <li>✅ Tap to Pay</li>
                  <li>✅ Instant Checkout</li>
                </ul>
                <span className="gateway-status connected">Connected</span>
              </div>
            </div>
          </section>
        )}
      </div>
    </div>
  );
};

export default Payment;
