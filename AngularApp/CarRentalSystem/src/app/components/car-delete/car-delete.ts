import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Car } from '../../models/car';
import { AdminService } from '../../service/admin-service';

@Component({
  selector: 'app-car-delete',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './car-delete.html',
  styleUrl: './car-delete.css'
})
export class CarDelete {
  cars: Car[] = [];
  message: string = '';
  token: string;

  constructor(private _adminService: AdminService) {
   
    this.token = localStorage.getItem("jwt") || '';
  }

  ngOnInit(): void {
    console.log("Token used in car-delete:", this.token);
    this.getAllCars();
  }

  getAllCars(): void {
    this._adminService.showCars(this.token).subscribe({
      next: (data) => this.cars = data,
      error: (err) => console.error('Error fetching cars:', err)
    });
  }

  deleteCar(carId: number): void {
    if (confirm('Are you sure you want to delete this car?')) {
      this._adminService.deleteCar(carId, this.token).subscribe({
        next: (res) => {
          this.message = res;
          this.getAllCars(); 
        },
        error: (err) => {
          this.message = 'Failed to delete car.';
          console.error('Delete car error:', err);
        }
      });
    }
  }
}
