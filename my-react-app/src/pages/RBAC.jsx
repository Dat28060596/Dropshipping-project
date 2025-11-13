import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/RBAC.css';

const RBAC = () => {
  const navigate = useNavigate();
  const [users, setUsers] = useState([
    { id: 1, username: 'admin', email: 'admin@example.com', fullName: 'Admin User', roles: ['ADMIN'], status: 'Active' },
    { id: 2, username: 'staff', email: 'staff@example.com', fullName: 'Staff User', roles: ['STAFF'], status: 'Active' },
    { id: 3, username: 'supplier', email: 'supplier@example.com', fullName: 'Supplier User', roles: ['SUPPLIER'], status: 'Active' },
    { id: 4, username: 'user', email: 'user@example.com', fullName: 'Regular User', roles: ['USER'], status: 'Active' }
  ]);

  const [editingUser, setEditingUser] = useState(null);
  const [showRoleModal, setShowRoleModal] = useState(false);

  const availableRoles = ['ADMIN', 'STAFF', 'SUPPLIER', 'USER'];

  const rolePermissions = {
    ADMIN: ['View Dashboard', 'Manage Users', 'View Analytics', 'Manage Roles', 'Configure System', 'View Payments'],
    STAFF: ['View Dashboard', 'View Orders', 'Update Orders', 'View Analytics', 'View Payments'],
    SUPPLIER: ['View Dashboard', 'Manage Products', 'View Orders', 'View Analytics'],
    USER: ['View Dashboard', 'Place Orders', 'View Order Status']
  };

  const handleEditUser = (user) => {
    setEditingUser(user);
    setShowRoleModal(true);
  };

  const handleRoleChange = (roleToToggle) => {
    if (!editingUser) return;

    const newRoles = editingUser.roles.includes(roleToToggle)
      ? editingUser.roles.filter(r => r !== roleToToggle)
      : [...editingUser.roles, roleToToggle];

    setEditingUser({ ...editingUser, roles: newRoles });
  };

  const handleSaveRoles = () => {
    setUsers(users.map(u => u.id === editingUser.id ? editingUser : u));
    setShowRoleModal(false);
    setEditingUser(null);
  };

  const handleDisableUser = (userId) => {
    setUsers(users.map(u =>
      u.id === userId ? { ...u, status: u.status === 'Active' ? 'Disabled' : 'Active' } : u
    ));
  };

  return (
    <div className="rbac-container">
      <header className="page-header">
        <button className="back-btn" onClick={() => navigate('/dashboard')}>
          ← Back to Dashboard
        </button>
        <h1>🔐 User Roles & Permissions Management</h1>
      </header>

      <div className="rbac-content">
        <section className="users-section">
          <h2>👥 Users & Roles</h2>
          <div className="table-wrapper">
            <table className="users-table">
              <thead>
                <tr>
                  <th>Username</th>
                  <th>Full Name</th>
                  <th>Email</th>
                  <th>Roles</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {users.map(user => (
                  <tr key={user.id}>
                    <td>{user.username}</td>
                    <td>{user.fullName}</td>
                    <td>{user.email}</td>
                    <td>
                      <div className="roles-badges">
                        {user.roles.map(role => (
                          <span key={role} className={`role-badge role-${role.toLowerCase()}`}>
                            {role}
                          </span>
                        ))}
                      </div>
                    </td>
                    <td>
                      <span className={`status-badge status-${user.status.toLowerCase()}`}>
                        {user.status}
                      </span>
                    </td>
                    <td>
                      <button
                        className="action-btn edit-btn"
                        onClick={() => handleEditUser(user)}
                      >
                        ✏️ Edit Roles
                      </button>
                      <button
                        className="action-btn toggle-btn"
                        onClick={() => handleDisableUser(user.id)}
                      >
                        {user.status === 'Active' ? '🔒 Disable' : '🔓 Enable'}
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>

        <section className="permissions-section">
          <h2>📋 Role Permissions Matrix</h2>
          <div className="permissions-matrix">
            <table className="permissions-table">
              <thead>
                <tr>
                  <th>Permission</th>
                  {availableRoles.map(role => (
                    <th key={role}>{role}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {Object.entries(rolePermissions).length > 0 && (
                  <>
                    {['View Dashboard', 'Manage Users', 'View Analytics', 'Manage Roles', 'Configure System', 'View Payments', 'Manage Products', 'Update Orders', 'Place Orders', 'View Order Status'].map(permission => (
                      <tr key={permission}>
                        <td className="permission-name">{permission}</td>
                        {availableRoles.map(role => (
                          <td key={`${role}-${permission}`} className="permission-cell">
                            {rolePermissions[role]?.includes(permission) ? (
                              <span className="permission-granted">✅</span>
                            ) : (
                              <span className="permission-denied">❌</span>
                            )}
                          </td>
                        ))}
                      </tr>
                    ))}
                  </>
                )}
              </tbody>
            </table>
          </div>
        </section>
      </div>

      {showRoleModal && editingUser && (
        <div className="modal-overlay" onClick={() => setShowRoleModal(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <h3>Edit Roles for {editingUser.fullName}</h3>
            <div className="role-checkboxes">
              {availableRoles.map(role => (
                <label key={role} className="role-checkbox">
                  <input
                    type="checkbox"
                    checked={editingUser.roles.includes(role)}
                    onChange={() => handleRoleChange(role)}
                  />
                  <span>{role}</span>
                </label>
              ))}
            </div>
            <div className="modal-actions">
              <button className="btn-cancel" onClick={() => setShowRoleModal(false)}>
                Cancel
              </button>
              <button className="btn-save" onClick={handleSaveRoles}>
                Save Roles
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default RBAC;
