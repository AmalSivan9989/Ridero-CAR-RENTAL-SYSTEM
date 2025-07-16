import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CustomerService } from '../../service/customer-service';

@Component({
  selector: 'app-payment-history',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './payment-history.html',
  styleUrl: './payment-history.css'
})
export class PaymentHistory implements OnInit {
  paymentHistory: any[] = [];

  constructor(private _customerService: CustomerService) {}

  ngOnInit(): void {
    this.loadPaymentHistory();
  }

  loadPaymentHistory(): void {
    const token = localStorage.getItem('jwt');
    const userIdString = localStorage.getItem('cuid');

    if (!token || !userIdString) {
      alert('You are not logged in.');
      return;
    }

    const userId = Number(userIdString);
    if (isNaN(userId)) {
      alert('Invalid user ID.');
      return;
    }

    this._customerService.paymentHistory(userId, token).subscribe({
      next: (data) => {
        this.paymentHistory = data;
      },
      error: (err) => {
        console.error('Error fetching payment history:', err);
        alert('Failed to load payment history.');
      }
    });
  }
}
