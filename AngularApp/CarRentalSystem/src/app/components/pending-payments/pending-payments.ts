import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { CustomerService } from '../../service/customer-service';
import { Booking } from '../../models/booking';

@Component({
  selector: 'app-pending-payments',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './pending-payments.html',
  styleUrl: './pending-payments.css'
})
export class PendingPayments implements OnInit {
  pendingPayments: Booking[] = [];

  constructor(private _customerService: CustomerService, private router: Router) {}

  ngOnInit(): void {
    this.loadPendingPayments();
  }

  loadPendingPayments(): void {
    const token = localStorage.getItem('jwt');
    const userIdString = localStorage.getItem('cuid');

    if (!userIdString || !token) {
      alert('User ID or token missing. Please login.');
      this.router.navigate(['/customerLogin']);
      return;
    }

    const userId = Number(userIdString);
    if (isNaN(userId)) {
      alert('Invalid user ID. Please login again.');
      this.router.navigate(['/customerLogin']);
      return;
    }

    this._customerService.getPendingPayments(userId, token).subscribe({
      next: data => {
        this.pendingPayments = data;
      },
      error: err => {
        console.error('Error fetching pending payments:', err);
        alert('Failed to load pending payments. Please login again.');
        this.router.navigate(['/customerMenu']);
      }
    });
  }

 makePayment(bookingId: number, amount: number, form: NgForm) {
  if (form.invalid) {
    alert('Please select a payment mode.');
    return;
  }

  const token = localStorage.getItem('jwt');
  if (!token) {
    alert('You are not logged in');
    this.router.navigate(['/customerLogin']);
    return;
  }

  const transactionId = 'TXN-' + Math.floor(Math.random() * 1000000000);

  const paymentData = {
    amount: amount,
    paymentMode: form.value.paymentMode,
    transactionId: transactionId
  };

  this._customerService.makePayment(bookingId, paymentData, token).subscribe({
    next: () => {
      alert('Payment successful! Transaction ID: ' + transactionId);

      this._customerService.checkInBooking(bookingId, token).subscribe({
        next: () => {
          console.log('Auto check-in done.');
          
          
          this.pendingPayments = this.pendingPayments.filter(b => b.id !== bookingId);

          
          setTimeout(() => {
            this.router.navigate(['/customerMenu/myBookings']);
          }, 500);

        },
        error: err => {
          console.error('Check-in failed:', err);
          alert('Payment successful, but check-in failed.');
        }
      });
    },
    error: (err) => {
      console.error('Payment failed:', err);
      alert('Payment failed. Please try again.');
    }
  });
}
}