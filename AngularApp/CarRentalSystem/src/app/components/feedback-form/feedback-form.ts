import { Component } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AdminService } from '../../service/admin-service';
import { FormsModule, NgForm } from '@angular/forms';
import { CustomerService } from '../../service/customer-service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-feedback-form',
  imports: [CommonModule,FormsModule,RouterModule],
  templateUrl: './feedback-form.html',
  styleUrl: './feedback-form.css'
})
export class FeedbackForm {
carId!: number;
  feedbackText: string = '';
  rating: number = 5;
  message: string = '';

hoveredRating: number = 0;

  constructor(private route: ActivatedRoute, private service: CustomerService, private router: Router) {
    this.carId = Number(this.route.snapshot.paramMap.get('carId'));
  }

  submitFeedback(form: NgForm): void {
    if (form.invalid) {
      return;
    }

    const token = localStorage.getItem('jwt');
    if (!token) {
      alert('Please login');
      this.router.navigate(['/customerLogin']);
      return;
    }

    const feedback = {
      comment: this.feedbackText,
      rating: this.rating
    };

    this.service.submitFeedback(this.carId, feedback, token).subscribe({
      next: res => {
        this.message = 'Feedback submitted successfully!';
        setTimeout(() => {
          this.router.navigate(['/customerMenu']);
        }, 1500);
      },
      error: err => {
        console.error(err);
        this.message = 'Submission failed. Try again.';
      }
    });
  }
}