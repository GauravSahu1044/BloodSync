import React, { useState, useEffect } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { bloodDonationApi, hospitalApi } from '../services/api';
import { Hospital } from '../types';
import { useAuth } from '../services/auth';
import { toast } from 'react-toastify';

const DonationSchema = Yup.object().shape({
  hospitalId: Yup.number().required('Required'),
  bloodType: Yup.string().required('Required'),
  quantity: Yup.number()
    .min(1, 'Must be at least 1 unit')
    .required('Required'),
});

const BloodDonationPage = () => {
  const [hospitals, setHospitals] = useState<Hospital[]>([]);
  const { user } = useAuth();

  useEffect(() => {
    fetchHospitals();
  }, []);

  const fetchHospitals = async () => {
    try {
      const response = await hospitalApi.getAllHospitals();
      setHospitals(response.data);
    } catch (error) {
      toast.error('Failed to fetch hospitals');
    }
  };

  return (
    <div className="container mt-4">
      <div className="row justify-content-center">
        <div className="col-md-8">
          <div className="card">
            <div className="card-body">
              <h2 className="text-center mb-4">Blood Donation</h2>
              <Formik
                initialValues={{
                  hospitalId: '',
                  bloodType: user?.bloodType || '',
                  quantity: '',
                }}
                validationSchema={DonationSchema}
                onSubmit={async (values, { resetForm }) => {
                  try {
                    await bloodDonationApi.createDonation({
                      ...values,
                      donorId: user?.id,
                      status: 'pending',
                    });
                    toast.success('Donation request submitted successfully');
                    resetForm();
                  } catch (error) {
                    toast.error('Failed to submit donation request');
                  }
                }}
              >
                {({ isSubmitting }) => (
                  <Form>
                    <div className="mb-3">
                      <label htmlFor="hospitalId" className="form-label">
                        Select Hospital
                      </label>
                      <Field
                        as="select"
                        name="hospitalId"
                        className="form-control"
                      >
                        <option value="">Choose a hospital</option>
                        {hospitals.map((hospital) => (
                          <option key={hospital.id} value={hospital.id}>
                            {hospital.name}
                          </option>
                        ))}
                      </Field>
                      <ErrorMessage
                        name="hospitalId"
                        component="div"
                        className="text-danger"
                      />
                    </div>

                    <div className="mb-3">
                      <label htmlFor="bloodType" className="form-label">
                        Blood Type
                      </label>
                      <Field
                        type="text"
                        name="bloodType"
                        className="form-control"
                        disabled
                      />
                      <ErrorMessage
                        name="bloodType"
                        component="div"
                        className="text-danger"
                      />
                    </div>

                    <div className="mb-3">
                      <label htmlFor="quantity" className="form-label">
                        Quantity (units)
                      </label>
                      <Field
                        type="number"
                        name="quantity"
                        className="form-control"
                        min="1"
                      />
                      <ErrorMessage
                        name="quantity"
                        component="div"
                        className="text-danger"
                      />
                    </div>

                    <button
                      type="submit"
                      className="btn btn-primary w-100"
                      disabled={isSubmitting}
                    >
                      {isSubmitting ? 'Submitting...' : 'Submit Donation'}
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

export default BloodDonationPage;
