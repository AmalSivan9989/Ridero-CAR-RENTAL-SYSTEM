import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AdminService } from '../../service/admin-service';
import html2pdf from 'html2pdf.js';

@Component({
  selector: 'app-full-summary-report',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './full-summary-report.html',
  styleUrl: './full-summary-report.css'
})
export class FullSummaryReport implements OnInit {
  summary: any = {};
  error = '';

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    const token = localStorage.getItem('jwt');
    if (!token) {
      this.error = 'Unauthorized access';
      return;
    }

    this.adminService.getFullSummary(token).subscribe({
      next: (data) => {
        console.log("Summary data:", data);
        this.summary = data;
      },
      error: (err) => {
        console.error("Error fetching summary", err);
        this.error = 'Failed to fetch summary';
      }
    });
  }

  printReport() {
  setTimeout(() => {
    const element = document.getElementById('print-section');
    if (!element) {
      console.error("Print section not found");
      return;
    }

    const options = {
      margin: 0.5,
      filename: 'summary-report.pdf',
      image: { type: 'jpeg', quality: 0.98 },
      html2canvas: { scale: 2 },
      jsPDF: { unit: 'in', format: 'a4', orientation: 'portrait' }
    };

    html2pdf().from(element).set(options).save();
  }, 500); 
}
}