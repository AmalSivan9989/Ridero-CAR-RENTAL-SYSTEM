import { Component } from '@angular/core';
import { Car } from '../../models/car';
import { CustomerService } from '../../service/customer-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-show-cars-by-location',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './show-cars-by-location.html',
  styleUrls: ['./show-cars-by-location.css']  
})
export class ShowCarsByLocation {
  cars: Car[] = [];
  location: string = '';
  token: string | null = localStorage.getItem('jwt');
  

  viewMode: 'table' | 'cards' = 'table';

  constructor(
    private _customerService: CustomerService,
    private router: Router
  ) {}

  fetchCars(location: string): void {
    if (!this.token) {
      alert('You are not logged in.');
      this.router.navigate(['/customerLogin']);
      return;
    }

    if (!location.trim()) {
      alert('Please enter a location.');
      return;
    }

    this._customerService.getCarsByLocation(this.token, location).subscribe({
      next: (data) => this.cars = data,
      error: (err) => alert('Error fetching cars: ' + err.message)
    });
  }

  bookCar(carId: number): void {
    localStorage.setItem('selectedCarId', String(carId));
    this.router.navigate(['/customerMenu/createBooking']);
  }
}
