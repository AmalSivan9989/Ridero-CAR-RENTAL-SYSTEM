import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-customer-menu',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './customer-menu.html',
  styleUrl: './customer-menu.css'
})
export class CustomerMenu {
  constructor(private router: Router) {}

  goToBooking() {
    const carId = localStorage.getItem('selectedCarId');
    if (!carId) {
      alert('Please search and select a car first.');
    } else {
      this.router.navigate(['/customerMenu/createBooking']);
    }
  }

  logout() {
    localStorage.removeItem('jwt');
    localStorage.removeItem('cid');
    localStorage.removeItem('cuid');
    localStorage.removeItem('selectedCarId');
    this.router.navigate(['/customerLogin']);
  }
}
