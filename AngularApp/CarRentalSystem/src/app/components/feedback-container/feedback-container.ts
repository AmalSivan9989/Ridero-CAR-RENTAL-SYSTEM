import { Component, OnInit } from '@angular/core';
import { Car } from '../../models/car';
import { AdminService } from '../../service/admin-service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-feedback-container',
  imports: [CommonModule,FormsModule
  ],
  templateUrl: './feedback-container.html',
  styleUrl: './feedback-container.css'
})
export class FeedbackContainer implements OnInit {
  cars: Car[] = [];
  message: string = '';

  constructor(private adminService: AdminService,private router: Router) {}

  ngOnInit(): void {
    const token = localStorage.getItem('jwt');
    if (!token) {
      this.message = 'Token not found';
      return;
    }

    this.adminService.showCars(token).subscribe({
      next: (data) => {
        this.cars = data;
        this.message = '';
      },
      error: () => {
        this.message = 'Failed to load cars';
      }
    });
  }

  seeFeedback(carId: number) {
  this.router.navigate(['/adminMenu/carFeedback', carId]);
}
}