import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider } from './context/AuthContext';
import Navbar from './components/Navbar';
import ProtectedRoute from './components/ProtectedRoute';

// Public Pages
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import ForgotPassword from './pages/ForgotPassword';
import PublicVerification from './pages/PublicVerification';
import About from './pages/About';
import Contact from './pages/Contact';
import EligibilityChecker from './pages/EligibilityChecker';

// Donor Pages
import DonorDashboard from './pages/donor/Dashboard';
import CreateProfile from './pages/donor/CreateProfile';
import UploadConsent from './pages/donor/UploadConsent';

// Hospital Pages
import HospitalDashboard from './pages/hospital/Dashboard';
import HospitalLayout from './pages/hospital/HospitalLayout';
import CreateHospitalProfile from './pages/hospital/CreateHospitalProfile';
import SearchDonor from './pages/hospital/SearchDonor';
import UploadDeathCert from './pages/hospital/UploadDeathCert';
import RegisterOrgan from './pages/hospital/RegisterOrgan';
import RegisterRecipient from './pages/hospital/RegisterRecipient';
import TransplantsTracker from './pages/hospital/TransplantsTracker';
import ViewOrgans from './pages/hospital/ViewOrgans';
import ViewRecipients from './pages/hospital/ViewRecipients';

// Admin Pages
import AdminDashboard from './pages/admin/Dashboard';

function App() {
  return (
    <Router>
      <AuthProvider>
        <div className="flex flex-col min-h-screen bg-slate-50 font-sans">
          <Navbar />
          <div className="flex-grow">
            <Routes>
              {/* Public Routes */}
              <Route path="/" element={<Home />} />
              <Route path="/about" element={<About />} />
              <Route path="/contact" element={<Contact />} />
              <Route path="/eligibility-checker" element={<EligibilityChecker />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/forgot-password" element={<ForgotPassword />} />
              <Route path="/verify" element={<PublicVerification />} />

              {/* Protected Routes - Donor */}
              <Route element={<ProtectedRoute allowedRoles={['DONOR']} />}>
                <Route path="/donor/dashboard" element={<DonorDashboard />} />
                <Route path="/donor/create-profile" element={<CreateProfile />} />
                <Route path="/donor/upload-consent" element={<UploadConsent />} />
              </Route>

              {/* Protected Routes - Hospital */}
              <Route element={<ProtectedRoute allowedRoles={['HOSPITAL']} />}>
                <Route element={<HospitalLayout />}>
                  <Route path="/hospital/create-profile" element={<CreateHospitalProfile />} />
                  <Route path="/hospital/dashboard" element={<HospitalDashboard />} />
                  <Route path="/hospital/search-donor" element={<SearchDonor />} />
                  <Route path="/hospital/register-organ" element={<RegisterOrgan />} />
                  <Route path="/hospital/register-recipient" element={<RegisterRecipient />} />
                  <Route path="/hospital/upload-death-cert" element={<UploadDeathCert />} />
                  <Route path="/hospital/transplants" element={<TransplantsTracker />} />
                  <Route path="/hospital/organs" element={<ViewOrgans />} />
                  <Route path="/hospital/recipients" element={<ViewRecipients />} />
                </Route>
              </Route>

              {/* Protected Routes - Admin */}
              <Route element={<ProtectedRoute allowedRoles={['ADMIN']} />}>
                <Route path="/admin/dashboard" element={<AdminDashboard />} />
              </Route>

              {/* Unauthorized */}
              <Route path="/unauthorized" element={<div className="p-20 text-center text-2xl font-bold text-red-500">Unauthorized Access</div>} />
              
              {/* Catch-all 404 Route */}
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </div>
          <Toaster 
            position="top-right"
            toastOptions={{
              className: '',
              style: {
                border: '1px solid #f1f5f9',
                padding: '16px',
                color: '#0f172a',
                borderRadius: '16px',
                boxShadow: '0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05)',
              },
            }}
          />
        </div>
      </AuthProvider>
    </Router>
  );
}

export default App;
