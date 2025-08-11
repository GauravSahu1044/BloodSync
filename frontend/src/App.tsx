import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { AuthProvider, useAuth } from './services/auth';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Register from './pages/Register';
import HospitalsPage from './pages/HospitalsPage';
import UsersPage from './pages/UsersPage';
import BloodInventoryPage from './pages/BloodInventoryPage';
import BloodDonationPage from './pages/BloodDonationPage';
import BloodRequestPage from './pages/BloodRequestPage';

// Protected Route Component
const ProtectedRoute: React.FC<{
  element: React.ReactElement;
  allowedRoles?: string[];
}> = ({ element, allowedRoles }) => {
  const { isAuthenticated, user } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }

  if (allowedRoles && user && !allowedRoles.includes(user.role)) {
    return <Navigate to="/" />;
  }

  return element;
};

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="min-vh-100 d-flex flex-column">
          <Navbar />
          <main className="flex-grow-1">
            <Routes>
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              
              {/* Admin Routes */}
              <Route
                path="/hospitals"
                element={
                  <ProtectedRoute
                    element={<HospitalsPage />}
                    allowedRoles={['admin']}
                  />
                }
              />
              <Route
                path="/users"
                element={
                  <ProtectedRoute
                    element={<UsersPage />}
                    allowedRoles={['admin']}
                  />
                }
              />
              <Route
                path="/inventory"
                element={
                  <ProtectedRoute
                    element={<BloodInventoryPage />}
                    allowedRoles={['admin']}
                  />
                }
              />

              {/* Donor Routes */}
              <Route
                path="/donate"
                element={
                  <ProtectedRoute
                    element={<BloodDonationPage />}
                    allowedRoles={['donor']}
                  />
                }
              />

              {/* Patient Routes */}
              <Route
                path="/request"
                element={
                  <ProtectedRoute
                    element={<BloodRequestPage />}
                    allowedRoles={['patient']}
                  />
                }
              />

              {/* Default Route */}
              <Route
                path="/"
                element={
                  <div className="container mt-5 text-center">
                    <h1>Welcome to Blood Bank Management System</h1>
                    <p className="lead">
                      A centralized platform for blood donation and requests
                    </p>
                  </div>
                }
              />
            </Routes>
          </main>
          <ToastContainer />
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
