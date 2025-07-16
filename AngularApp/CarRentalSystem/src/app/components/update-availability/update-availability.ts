import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AdminService } from '../../service/admin-service';
import { Car } from '../../models/car';

@Component({
  selector: 'app-update-availability',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './update-availability.html',
  styleUrl: './update-availability.css'
})
export class UpdateAvailability implements OnInit {

  cars: Car[] = [];
  result: string = '';

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadCars();
  }

  loadCars(): void {
    const token = localStorage.getItem("jwt");
    if (!token) {
      this.result = 'Unauthorized';
      return;
    }

    this.adminService.showCars(token).subscribe({
      next: (data) => this.cars = data,
      error: () => this.result = 'Failed to load cars'
    });
  }

  updateAvailabilityForCar(car: Car): void {
    const token = localStorage.getItem('jwt');
    if (!token) {
      this.result = 'User not authenticated';
      return;
    }

    this.adminService.updateAvailability(car.id, car.available, token).subscribe({
      next: () => {
        this.result = `Car ID ${car.id} availability updated to ${car.available}`;
        this.loadCars(); 
      },
      error: () => {
        this.result = `Failed to update car ID ${car.id}`;
      }
    });
  }
}
