import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../services/auth';

const Navbar = () => {
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
      <div className="container">
        <Link className="navbar-brand" to="/">
          Blood Bank Management
        </Link>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav ms-auto">
            {!isAuthenticated ? (
              <>
                <li className="nav-item">
                  <Link className="nav-link" to="/login">
                    Login
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/register">
                    Register
                  </Link>
                </li>
              </>
            ) : (
              <>
                {user?.role === 'admin' && (
                  <>
                    <li className="nav-item">
                      <Link className="nav-link" to="/hospitals">
                        Hospitals
                      </Link>
                    </li>
                    <li className="nav-item">
                      <Link className="nav-link" to="/users">
                        Users
                      </Link>
                    </li>
                    <li className="nav-item">
                      <Link className="nav-link" to="/inventory">
                        Blood Inventory
                      </Link>
                    </li>
                  </>
                )}
                {user?.role === 'donor' && (
                  <li className="nav-item">
                    <Link className="nav-link" to="/donate">
                      Donate Blood
                    </Link>
                  </li>
                )}
                {user?.role === 'patient' && (
                  <li className="nav-item">
                    <Link className="nav-link" to="/request">
                      Request Blood
                    </Link>
                  </li>
                )}
                <li className="nav-item">
                  <button
                    className="nav-link btn btn-link"
                    onClick={handleLogout}
                  >
                    Logout
                  </button>
                </li>
              </>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
