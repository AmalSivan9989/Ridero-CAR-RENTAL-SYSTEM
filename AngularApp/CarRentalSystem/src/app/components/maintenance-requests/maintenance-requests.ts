import { Component, OnInit } from '@angular/core';
import { MaintenanceRequest } from '../../models/maintenance-request';
import { AgentService } from '../../service/agent-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-maintenance-requests',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './maintenance-requests.html',
  styleUrls: ['./maintenance-requests.css']
})
export class MaintenanceRequests implements OnInit {
  requests: MaintenanceRequest[] = [];
  loading: boolean = false;
  error: string | null = null;
  successMessage: string | null = null;

  agentId: number | null;

  private token: string | null;

  constructor(private agentService: AgentService) {
    this.token = localStorage.getItem('jwt');
    const idStr = localStorage.getItem('auid');
    this.agentId = idStr ? Number(idStr) : null;
  }

  ngOnInit() {
    this.loadRequests();
  }

  loadRequests() {
    if (!this.token || !this.agentId) {
      this.error = 'Agent not authenticated properly.';
      this.loading = false;
      return;
    }

    this.loading = true;
    this.error = null;
    this.successMessage = null;

    
    this.agentService.getRequestsIncludingUnassigned(this.agentId, this.token).subscribe({
      next: (data) => {
        this.requests = data.filter(req => req.status?.toUpperCase() !== 'CLOSED');
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load requests:', err);
        this.error = 'Failed to load maintenance requests.';
        this.loading = false;
      }
    });
  }

 assignToMe(requestId: number) {
  if (!this.token || !this.agentId) {
    this.error = 'Agent not authenticated properly.';
    return;
  }

  this.loading = true;
  this.error = null;
  this.successMessage = null;

  this.agentService.assignRequestToAgent(requestId, this.agentId, this.token).subscribe({
    next: (response) => {
      if (response.status >= 200 && response.status < 300) {
        this.successMessage = response.body || 'Request assigned to you successfully.';
        
        this.loadRequests();
      } else {
        this.error = 'Unexpected response from server while assigning request.';
      }
      this.loading = false;
    },
    error: (err) => {
      console.error('Assign request error:', err);
      this.error = 'Failed to assign maintenance request.';
      this.loading = false;
    }
  });
}

closeRequest(requestId: number) {
  if (!this.token || !this.agentId) {
    this.error = 'Agent not authenticated properly.';
    return;
  }

  this.loading = true;
  this.error = null;
  this.successMessage = null;

  this.agentService.closeMaintenanceRequest(requestId, this.agentId, this.token).subscribe({
    next: (response) => {
      if (response.status >= 200 && response.status < 300) {
        this.successMessage = response.body || 'Request closed successfully.';
        
        this.loadRequests();
      } else {
        this.error = 'Unexpected response from server while closing request.';
      }
      this.loading = false;
    },
    error: (err) => {
      console.error('Close request error:', err);
      this.error = 'Failed to close maintenance request.';
      this.loading = false;
    }
  });
}
}