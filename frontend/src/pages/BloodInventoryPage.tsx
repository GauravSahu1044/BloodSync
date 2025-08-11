import React, { useState, useEffect } from 'react';
import { bloodInventoryApi } from '../services/api';
import { BloodInventory } from '../types';
import DataTable from '../components/DataTable';
import DashboardCard from '../components/DashboardCard';
import { toast } from 'react-toastify';

const BloodInventoryPage = () => {
  const [inventory, setInventory] = useState<BloodInventory[]>([]);

  useEffect(() => {
    fetchInventory();
  }, []);

  const fetchInventory = async () => {
    try {
      const response = await bloodInventoryApi.getAllInventory();
      setInventory(response.data);
    } catch (error) {
      toast.error('Failed to fetch blood inventory');
    }
  };

  const calculateTotalUnits = (bloodType: string) => {
    return inventory
      .filter((item) => item.bloodType === bloodType)
      .reduce((acc, item) => acc + item.quantity, 0);
  };

  const columns = [
    { key: 'hospitalId', header: 'Hospital ID' },
    { key: 'bloodType', header: 'Blood Type' },
    { key: 'quantity', header: 'Quantity (units)' },
    {
      key: 'lastUpdated',
      header: 'Last Updated',
      render: (item: BloodInventory) =>
        new Date(item.lastUpdated).toLocaleDateString(),
    },
  ];

  return (
    <div className="container mt-4">
      <h2 className="mb-4">Blood Inventory</h2>

      <div className="row mb-4">
        <DashboardCard
          title="A+ Blood"
          count={calculateTotalUnits('A+')}
          icon="bi-droplet-fill"
          color="#dc3545"
        />
        <DashboardCard
          title="B+ Blood"
          count={calculateTotalUnits('B+')}
          icon="bi-droplet-fill"
          color="#28a745"
        />
        <DashboardCard
          title="AB+ Blood"
          count={calculateTotalUnits('AB+')}
          icon="bi-droplet-fill"
          color="#17a2b8"
        />
        <DashboardCard
          title="O+ Blood"
          count={calculateTotalUnits('O+')}
          icon="bi-droplet-fill"
          color="#ffc107"
        />
      </div>

      <div className="row mb-4">
        <DashboardCard
          title="A- Blood"
          count={calculateTotalUnits('A-')}
          icon="bi-droplet-fill"
          color="#dc3545"
        />
        <DashboardCard
          title="B- Blood"
          count={calculateTotalUnits('B-')}
          icon="bi-droplet-fill"
          color="#28a745"
        />
        <DashboardCard
          title="AB- Blood"
          count={calculateTotalUnits('AB-')}
          icon="bi-droplet-fill"
          color="#17a2b8"
        />
        <DashboardCard
          title="O- Blood"
          count={calculateTotalUnits('O-')}
          icon="bi-droplet-fill"
          color="#ffc107"
        />
      </div>

      <div className="card">
        <div className="card-body">
          <h4>Detailed Inventory</h4>
          <DataTable data={inventory} columns={columns} />
        </div>
      </div>
    </div>
  );
};

export default BloodInventoryPage;
