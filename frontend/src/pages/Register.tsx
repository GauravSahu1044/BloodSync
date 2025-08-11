import React from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { useNavigate } from 'react-router-dom';
import { authApi } from '../services/api';
import { toast } from 'react-toastify';

const RegisterSchema = Yup.object().shape({
  username: Yup.string().required('Required'),
  email: Yup.string().email('Invalid email').required('Required'),
  password: Yup.string()
    .min(6, 'Password must be at least 6 characters')
    .required('Required'),
  fullName: Yup.string().required('Required'),
  age: Yup.number()
    .min(18, 'Must be at least 18 years old')
    .required('Required'),
  gender: Yup.string().required('Required'),
  bloodType: Yup.string().required('Required'),
  contact: Yup.string().required('Required'),
  address: Yup.string().required('Required'),
  role: Yup.string().required('Required'),
});

const Register = () => {
  const navigate = useNavigate();

  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-8">
          <div className="card shadow">
            <div className="card-body">
              <h2 className="text-center mb-4">Register</h2>
              <Formik
                initialValues={{
                  username: '',
                  email: '',
                  password: '',
                  fullName: '',
                  age: '',
                  gender: '',
                  bloodType: '',
                  contact: '',
                  address: '',
                  role: 'user',
                }}
                validationSchema={RegisterSchema}
                onSubmit={async (values, { setSubmitting }) => {
                  try {
                    await authApi.register(values);
                    toast.success('Registration successful! Please login.');
                    navigate('/login');
                  } catch (error: any) {
                    toast.error(
                      error.response?.data?.message || 'Registration failed'
                    );
                  } finally {
                    setSubmitting(false);
                  }
                }}
              >
                {({ isSubmitting }) => (
                  <Form>
                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <label htmlFor="username" className="form-label">
                          Username
                        </label>
                        <Field
                          type="text"
                          name="username"
                          className="form-control"
                          placeholder="Choose a username"
                        />
                        <ErrorMessage
                          name="username"
                          component="div"
                          className="text-danger"
                        />
                      </div>

                      <div className="col-md-6 mb-3">
                        <label htmlFor="email" className="form-label">
                          Email
                        </label>
                        <Field
                          type="email"
                          name="email"
                          className="form-control"
                          placeholder="Enter your email"
                        />
                        <ErrorMessage
                          name="email"
                          component="div"
                          className="text-danger"
                        />
                      </div>
                    </div>

                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <label htmlFor="password" className="form-label">
                          Password
                        </label>
                        <Field
                          type="password"
                          name="password"
                          className="form-control"
                          placeholder="Choose a password"
                        />
                        <ErrorMessage
                          name="password"
                          component="div"
                          className="text-danger"
                        />
                      </div>

                      <div className="col-md-6 mb-3">
                        <label htmlFor="fullName" className="form-label">
                          Full Name
                        </label>
                        <Field
                          type="text"
                          name="fullName"
                          className="form-control"
                          placeholder="Enter your full name"
                        />
                        <ErrorMessage
                          name="fullName"
                          component="div"
                          className="text-danger"
                        />
                      </div>
                    </div>

                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <label htmlFor="age" className="form-label">
                          Age
                        </label>
                        <Field
                          type="number"
                          name="age"
                          className="form-control"
                          placeholder="Enter your age"
                        />
                        <ErrorMessage
                          name="age"
                          component="div"
                          className="text-danger"
                        />
                      </div>

                      <div className="col-md-6 mb-3">
                        <label htmlFor="gender" className="form-label">
                          Gender
                        </label>
                        <Field
                          as="select"
                          name="gender"
                          className="form-control"
                        >
                          <option value="">Select Gender</option>
                          <option value="male">Male</option>
                          <option value="female">Female</option>
                          <option value="other">Other</option>
                        </Field>
                        <ErrorMessage
                          name="gender"
                          component="div"
                          className="text-danger"
                        />
                      </div>
                    </div>

                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <label htmlFor="bloodType" className="form-label">
                          Blood Type
                        </label>
                        <Field
                          as="select"
                          name="bloodType"
                          className="form-control"
                        >
                          <option value="">Select Blood Type</option>
                          <option value="A+">A+</option>
                          <option value="A-">A-</option>
                          <option value="B+">B+</option>
                          <option value="B-">B-</option>
                          <option value="AB+">AB+</option>
                          <option value="AB-">AB-</option>
                          <option value="O+">O+</option>
                          <option value="O-">O-</option>
                        </Field>
                        <ErrorMessage
                          name="bloodType"
                          component="div"
                          className="text-danger"
                        />
                      </div>

                      <div className="col-md-6 mb-3">
                        <label htmlFor="contact" className="form-label">
                          Contact Number
                        </label>
                        <Field
                          type="text"
                          name="contact"
                          className="form-control"
                          placeholder="Enter your contact number"
                        />
                        <ErrorMessage
                          name="contact"
                          component="div"
                          className="text-danger"
                        />
                      </div>
                    </div>

                    <div className="mb-3">
                      <label htmlFor="address" className="form-label">
                        Address
                      </label>
                      <Field
                        as="textarea"
                        name="address"
                        className="form-control"
                        placeholder="Enter your address"
                      />
                      <ErrorMessage
                        name="address"
                        component="div"
                        className="text-danger"
                      />
                    </div>

                    <div className="mb-3">
                      <label htmlFor="role" className="form-label">
                        Role
                      </label>
                      <Field as="select" name="role" className="form-control">
                        <option value="user">Regular User</option>
                        <option value="donor">Blood Donor</option>
                        <option value="patient">Patient</option>
                      </Field>
                      <ErrorMessage
                        name="role"
                        component="div"
                        className="text-danger"
                      />
                    </div>

                    <button
                      type="submit"
                      className="btn btn-primary w-100"
                      disabled={isSubmitting}
                    >
                      {isSubmitting ? 'Registering...' : 'Register'}
                    </button>
                  </Form>
                )}
              </Formik>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Register;
