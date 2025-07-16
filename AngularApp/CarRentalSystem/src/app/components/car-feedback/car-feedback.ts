import { Component } from '@angular/core';
import { Feedback } from '../../models/feedback';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { AdminService } from '../../service/admin-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-car-feedback',
  imports: [CommonModule,FormsModule,RouterModule],
  templateUrl: './car-feedback.html',
  styleUrl: './car-feedback.css'
})
export class CarFeedback {
feedbacks: Feedback[] = [];
  carId: number;
  message: string = '';

  constructor(private route: ActivatedRoute, private adminService: AdminService) {}

  ngOnInit(): void {
    this.carId = Number(this.route.snapshot.paramMap.get('id'));

    const token = localStorage.getItem('jwt');
    if (!token) {
      this.message = 'Token not found';
      return;
    }

    this.adminService.getFeedbackByCar(this.carId, token).subscribe({
      next: (data) => this.feedbacks = data,
      error: () => this.message = 'Failed to load feedback'
    });
  }
}

