import { Component } from '@angular/core';
import { UserData } from '../../models/user-data';
import { MaintenanceRequest } from '../../models/maintenance-request';
import { AdminService } from '../../service/admin-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-agent-requests',
  imports: [CommonModule,FormsModule,RouterModule],
  templateUrl: './agent-requests.html',
  styleUrl: './agent-requests.css'
})
export class AgentRequests {
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
