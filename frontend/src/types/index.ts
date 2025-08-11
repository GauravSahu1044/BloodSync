export interface User {
  id?: number;
  username: string;
  email: string;
  password?: string;
  role: 'admin' | 'donor' | 'patient' | 'user';
  fullName: string;
  age: number;
  gender: string;
  bloodType: string;
  contact: string;
  address: string;
}

export interface Hospital {
  id?: number;
  name: string;
  address: string;
  contact: string;
}

export interface BloodInventory {
  id?: number;
  hospitalId: number;
  bloodType: string;
  quantity: number;
  lastUpdated: string;
}

export interface BloodDonation {
  id?: number;
  donorId: number;
  hospitalId: number;
  bloodType: string;
  quantity: number;
  donationDate: string;
  status: string;
}

export interface BloodRequest {
  id?: number;
  patientId: number;
  hospitalId: number;
  bloodType: string;
  quantity: number;
  requestDate: string;
  status: string;
}
