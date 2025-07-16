import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../../service/customer-service';
import { Booking } from '../../models/booking';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-my-bookings',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './my-bookings.html',
  styleUrl: './my-bookings.css'
})
export class MyBookings implements OnInit {
  bookings: Booking[] = [];
  token: string = '';
  userId: number = 0;

  constructor(private _customerService: CustomerService, private router: Router) {}

  ngOnInit(): void {
    this.token = localStorage.getItem('jwt') || '';
    this.userId = Number(localStorage.getItem('cuid'));
    if (this.token && this.userId) {
      this.loadMyBookings();
    } else {
      alert('You are not logged in.');
    }
  }

  loadMyBookings(): void {
    this._customerService.getMyBookings(this.token).subscribe({
      next: (data) => {
        this.bookings = data;
      },
      error: (err) => {
        console.error('Failed to fetch bookings:', err);
        alert('Could not load your bookings.');
      }
    });
  }

  checkOut(bookingId: number, carId: number): void {
    const token = localStorage.getItem('jwt') || '';

    this._customerService.checkOut(bookingId, token).subscribe({
      next: () => {
        const index = this.bookings.findIndex(b => b.id === bookingId);
        if (index !== -1) {
          const booking = this.bookings[index];
          booking.status = 'COMPLETED';

          const checkInDate = new Date(booking.checkInTime);
          const today = new Date();
          const diffInDays = Math.floor((today.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24));

          const refundAmount = booking.totalAmount ? booking.totalAmount * 0.6 : 0;

          if (diffInDays <= 2 && refundAmount > 0) {
            this._customerService.addFunds(this.userId, refundAmount, token).subscribe({
              next: () => {
                console.log(`₹${refundAmount} refunded successfully`);
                this.loadMyBookings();
                alert('Checked out successfully with refund!');
                this.router.navigate(['/feedback', carId]);
              },
              error: err => {
                console.error('Refund failed:', err);
                alert('Checkout done, but refund failed.');
                this.loadMyBookings();
                this.router.navigate(['/feedback', carId]);
              }
            });
          } else {
            console.warn('No refund applicable or totalAmount is missing.');
            this.loadMyBookings();
            alert('Checked out successfully!');
            this.router.navigate(['/feedback', carId]);
          }
        }
      },
      error: err => {
        console.error('Checkout failed:', err);
        alert('Checkout failed. Please try again.');
      }
    });
  }
}
