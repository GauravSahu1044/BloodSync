import React, { useState, useEffect } from 'react';
import { hospitalApi } from '../services/api';
import { Hospital } from '../types';
import DataTable from '../components/DataTable';
import { toast } from 'react-toastify';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';

const HospitalSchema = Yup.object().shape({
  name: Yup.string().required('Required'),
  address: Yup.string().required('Required'),
  contact: Yup.string().required('Required'),
});

const HospitalsPage = () => {
  const [hospitals, setHospitals] = useState<Hospital[]>([]);
  const [editingHospital, setEditingHospital] = useState<Hospital | null>(null);
  const [showForm, setShowForm] = useState(false);

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

  const handleSubmit = async (values: Partial<Hospital>, { resetForm }: any) => {
    try {
      if (editingHospital) {
        await hospitalApi.updateHospital(editingHospital.id!, values);
        toast.success('Hospital updated successfully');
      } else {
        await hospitalApi.createHospital(values);
        toast.success('Hospital added successfully');
      }
      resetForm();
      setShowForm(false);
      setEditingHospital(null);
      fetchHospitals();
    } catch (error) {
      toast.error('Operation failed');
    }
  };

  const handleEdit = (hospital: Hospital) => {
    setEditingHospital(hospital);
    setShowForm(true);
  };

  const handleDelete = async (hospital: Hospital) => {
    if (window.confirm('Are you sure you want to delete this hospital?')) {
      try {
        await hospitalApi.deleteHospital(hospital.id!);
        toast.success('Hospital deleted successfully');
        fetchHospitals();
      } catch (error) {
        toast.error('Failed to delete hospital');
      }
    }
  };

  const columns = [
    { key: 'name', header: 'Name' },
    { key: 'address', header: 'Address' },
    { key: 'contact', header: 'Contact' },
  ];

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Hospitals</h2>
        <button
          className="btn btn-primary"
          onClick={() => {
            setEditingHospital(null);
            setShowForm(true);
          }}
        >
          Add Hospital
        </button>
      </div>

      {showForm && (
        <div className="card mb-4">
          <div className="card-body">
            <h4>{editingHospital ? 'Edit Hospital' : 'Add New Hospital'}</h4>
            <Formik
              initialValues={{
                name: editingHospital?.name || '',
                address: editingHospital?.address || '',
                contact: editingHospital?.contact || '',
              }}
              validationSchema={HospitalSchema}
              onSubmit={handleSubmit}
            >
              {({ isSubmitting }) => (
                <Form>
                  <div className="mb-3">
                    <label htmlFor="name" className="form-label">
                      Name
                    </label>
                    <Field
                      type="text"
                      name="name"
                      className="form-control"
                      placeholder="Enter hospital name"
                    />
                    <ErrorMessage
                      name="name"
                      component="div"
                      className="text-danger"
                    />
                  </div>

                  <div className="mb-3">
                    <label htmlFor="address" className="form-label">
                      Address
                    </label>
                    <Field
                      type="text"
                      name="address"
                      className="form-control"
                      placeholder="Enter hospital address"
                    />
                    <ErrorMessage
                      name="address"
                      component="div"
                      className="text-danger"
                    />
                  </div>

                  <div className="mb-3">
                    <label htmlFor="contact" className="form-label">
                      Contact
                    </label>
                    <Field
                      type="text"
                      name="contact"
                      className="form-control"
                      placeholder="Enter contact information"
                    />
                    <ErrorMessage
                      name="contact"
                      component="div"
                      className="text-danger"
                    />
                  </div>

                  <div className="d-flex gap-2">
                    <button
                      type="submit"
                      className="btn btn-primary"
                      disabled={isSubmitting}
                    >
                      {isSubmitting
                        ? 'Saving...'
                        : editingHospital
                        ? 'Update'
                        : 'Add'}
                    </button>
                    <button
                      type="button"
                      className="btn btn-secondary"
                      onClick={() => {
                        setShowForm(false);
                        setEditingHospital(null);
                      }}
                    >
                      Cancel
                    </button>
                  </div>
                </Form>
              )}
            </Formik>
          </div>
        </div>
      )}

      <DataTable
        data={hospitals}
        columns={columns}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />
    </div>
  );
};

export default HospitalsPage;
