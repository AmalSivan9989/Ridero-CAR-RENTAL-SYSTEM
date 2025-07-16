import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Car } from '../../models/car';
import { AdminService } from '../../service/admin-service';
import { CarStatus } from '../../models/car-status';

@Component({
  selector: 'app-cars',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './cars.html',
  styleUrl: './cars.css'
})
export class Cars implements OnInit {
  cars: Car[] = [];
  token: string | null = '';
  isValid: boolean = false;
  result: string = '';

  car: Car = new Car();
  carStatuses = Object.values(CarStatus);

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.token = localStorage.getItem('jwt');
    if (this.token) {
      this.loadCars();
    } else {
      alert('Token not found. Please login again.');
    }
  }

  loadCars(): void {
    if (!this.token) return;

    this.adminService.showCars(this.token).subscribe({
      next: (data) => {
        this.cars = data;
      },
      error: (err) => {
        console.error('Error fetching cars', err);
        alert('Failed to load cars.');
      }
    });
  }

  addCar(carForm: NgForm): void {
    if (carForm.invalid) return;

    if (!this.token) {
      this.result = 'Token not found. Please login again.';
      return;
    }

    this.isValid = true;


    const carPayload = { ...this.car };
    delete carPayload.id;

    this.adminService.addCar(carPayload, this.token).subscribe({
      next: (savedCar) => {
        console.log('Car added:', savedCar);
        this.result = `Car added successfully.`;
        this.loadCars(); 
        this.car = new Car(); 
        carForm.resetForm();
      },
      error: (err) => {
        console.error('Error adding car', err);
        this.result = 'Failed to add car.';
      }
    });
  }

  updateAvailability(carId: number, available: boolean): void {
    if (!this.token) return;

    const confirmUpdate = confirm(`Are you sure you want to set availability to ${available} for Car ID ${carId}?`);
    if (!confirmUpdate) return;

    this.adminService.updateAvailability(carId, available, this.token).subscribe({
      next: () => {
        alert('Availability updated successfully.');
        this.loadCars();
      },
      error: (err) => {
        console.error('Error updating availability', err);
        alert('Failed to update availability.');
      }
    });
  }

  deleteCar(carId: number): void {
    if (!this.token) return;

    const confirmDelete = confirm(`Are you sure you want to delete Car ID ${carId}?`);
    if (!confirmDelete) return;

    this.adminService.deleteCar(carId, this.token).subscribe({
      next: () => {
        alert('Car deleted successfully.');
        this.loadCars();
      },
      error: (err) => {
        console.error('Error deleting car', err);
        alert('Failed to delete car.');
      }
    });
  }
}
