import { Component } from '@angular/core';
import { MaintenanceRequest } from '../../models/maintenance-request';
import { AdminService } from '../../service/admin-service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { UserData } from '../../models/user-data';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-view-agent-requests',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, FormsModule],
  templateUrl: './view-agent-requests.html',
  styleUrl: './view-agent-requests.css'
})
export class ViewAgentRequests {
  agents: UserData[] = [];
  selectedAgentId: number | null = null;
  requests: MaintenanceRequest[] = [];
  message: string = '';

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.fetchAgents();
  }

  fetchAgents() {
    const token = localStorage.getItem('jwt');
    if (!token) {
      this.message = 'Token not found';
      return;
    }

    this.adminService.getAllAgents(token).subscribe({
      next: (data) => {
        this.agents = data;

     
        if (this.agents.length > 0) {
          this.selectedAgentId = this.agents[0].uid;
          this.onAgentChange();
        }
      },
      error: () => this.message = 'Failed to load agents'
    });
  }

  onAgentChange() {
    if (!this.selectedAgentId) return;

    const token = localStorage.getItem('jwt');
    if (!token) {
      this.message = 'Token not found';
      return;
    }

    this.adminService.getRequestsByAgent(this.selectedAgentId, token).subscribe({
      next: (data) => {
        this.requests = data;
        this.message = ''; 
      },
      error: () => this.message = 'Failed to fetch requests'
    });
  }
}
