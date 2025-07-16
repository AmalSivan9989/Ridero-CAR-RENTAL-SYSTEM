import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import { MaintenanceRequest } from '../../models/maintenance-request';
import { AdminService } from '../../service/admin-service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Car } from '../../models/car';

@Component({
  selector: 'app-report-issue',
  imports: [FormsModule,ReactiveFormsModule,CommonModule,RouterModule],
  templateUrl: './report-issue.html',
  styleUrl: './report-issue.css'
})
export class ReportIssue {
 cars: Car[] = [];
  selectedCarId: number | null = null;
  description: string = '';
  message: string = '';
  token: string | null = '';

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.token = localStorage.getItem('jwt');
    if (!this.token) {
      this.message = 'Token not found';
      return;
    }

    this.adminService.showCars(this.token).subscribe({
      next: (data) => this.cars = data,
      error: () => this.message = 'Failed to load car list'
    });
  }

  toggleDescriptionBox(carId: number) {
    this.selectedCarId = this.selectedCarId === carId ? null : carId;
    this.description = ''; 
    this.message = '';
  }

  submitIssue(carId: number) {
    if (!this.token || !this.description.trim()) return;

    const request: MaintenanceRequest = {
      issueDescription: this.description.trim(),
      status: '',
      requestDate: new Date().toISOString()
    };

    this.adminService.reportIssue(carId, request, this.token).subscribe({
      next: (res) => {
        this.message = res;
        this.selectedCarId = null;
        this.description = '';
      },
      error: () => this.message = 'Failed to report issue'
    });
  }
}