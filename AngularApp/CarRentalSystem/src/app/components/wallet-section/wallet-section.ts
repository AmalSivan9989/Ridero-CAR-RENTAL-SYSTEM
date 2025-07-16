import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CustomerService } from '../../service/customer-service';

@Component({
  selector: 'app-wallet-section',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './wallet-section.html',
  styleUrl: './wallet-section.css'
})
export class WalletSection implements OnInit {
  balance: number = -1; 
  amountToAdd: number = 0;
  message: string = '';
  userId: number = 0;
  token: string = '';
  walletExists: boolean = false;

  constructor(private _customerService: CustomerService) {}

  ngOnInit(): void {
    this.token = localStorage.getItem('jwt') || '';
    this.userId = Number(localStorage.getItem('cuid'));
    if (!this.token || !this.userId) {
      alert('Login required.');
      return;
    }
    this.loadWalletBalance();
  }

  loadWalletBalance(): void {
    this._customerService.getWalletBalance(this.userId, this.token).subscribe({
      next: (wallet) => {
        this.balance = wallet.balance;
        this.walletExists = true;
      },
      error: (err) => {
        console.error('Failed to load wallet balance:', err);
        this.walletExists = false;
      }
    });
  }

  createWallet(): void {
    this._customerService.createWallet(this.userId, this.token).subscribe({
      next: (wallet) => {
        this.balance = wallet.balance;
        this.walletExists = true;
        this.message = 'Wallet created successfully.';
      },
      error: (err) => {
        console.error('Failed to create wallet:', err);
        this.message = 'Failed to create wallet. Please try again.';
      }
    });
  }

  addFunds(paymentMode: string, form?: any): void {
    this.message = '';

    if (!this.amountToAdd || this.amountToAdd <= 0) {
      this.message = 'Please enter a valid amount.';
      return;
    }

    if (!this.userId || !this.token) {
      this.message = 'Authentication error. Please login again.';
      return;
    }

    const transactionId = 'TXN-' + Math.floor(Math.random() * 1000000000);

    this._customerService.addFunds(this.userId, this.amountToAdd, this.token).subscribe({
      next: (wallet) => {
        this.balance = wallet.balance;
        this.message = `₹${this.amountToAdd} added successfully via ${paymentMode} (Transaction ID: ${transactionId})`;
        this.amountToAdd = 0;
        if (form) form.resetForm();
      },
      error: (err) => {
        console.error('Failed to add funds:', err);
        this.message = 'Failed to add funds. Please try again.';
      }
    });
  }
}
