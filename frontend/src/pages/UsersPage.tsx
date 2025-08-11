import React, { useState, useEffect } from 'react';
import { userApi } from '../services/api';
import { User } from '../types';
import DataTable from '../components/DataTable';
import { toast } from 'react-toastify';

const UsersPage = () => {
  const [users, setUsers] = useState<User[]>([]);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await userApi.getAllUsers();
      setUsers(response.data);
    } catch (error) {
      toast.error('Failed to fetch users');
    }
  };

  const handleDelete = async (user: User) => {
    if (window.confirm('Are you sure you want to delete this user?')) {
      try {
        await userApi.deleteUser(user.id!);
        toast.success('User deleted successfully');
        fetchUsers();
      } catch (error) {
        toast.error('Failed to delete user');
      }
    }
  };

  const columns = [
    { key: 'username', header: 'Username' },
    { key: 'email', header: 'Email' },
    { key: 'fullName', header: 'Full Name' },
    { key: 'role', header: 'Role' },
    { key: 'bloodType', header: 'Blood Type' },
    {
      key: 'status',
      header: 'Status',
      render: (user: User) => (
        <span className={`badge bg-${user.role === 'admin' ? 'danger' : user.role === 'donor' ? 'success' : 'primary'}`}>
          {user.role}
        </span>
      ),
    },
  ];

  return (
    <div className="container mt-4">
      <h2 className="mb-4">Users</h2>
      <DataTable
        data={users}
        columns={columns}
        onDelete={handleDelete}
      />
    </div>
  );
};

export default UsersPage;
