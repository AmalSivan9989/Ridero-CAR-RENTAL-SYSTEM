import { Component, OnInit } from '@angular/core';
import { MaintenanceRequest } from '../../models/maintenance-request';
import { AgentService } from '../../service/agent-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-agent-view-requests',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './agent-view-requests.html',
  styleUrls: ['./agent-view-requests.css']
})
export class AgentViewRequestsComponent implements OnInit {
  requests: MaintenanceRequest[] = [];
  message: string = '';

  constructor(private agentService: AgentService) {}

  ngOnInit(): void {
    this.fetchRequests();
  }

  fetchRequests() {
    const token = localStorage.getItem('jwt');
    const agentId = localStorage.getItem('agentId');

    if (!token || !agentId) {
      this.message = 'Missing token or agent ID';
      return;
    }

    this.agentService.getRequestsByAgent(+agentId, token).subscribe({
      next: (data) => this.requests = data,
      error: () => this.message = 'Failed to load requests'
    });
  }

  closeRequest(requestId: number) {
    const token = localStorage.getItem('jwt');
    const agentId = localStorage.getItem('agentId');

    if (!token || !agentId) {
      this.message = 'Missing token or agent ID';
      return;
    }

    this.agentService.closeMaintenanceRequest(requestId, +agentId, token).subscribe({
      next: (res) => {
  this.message = res.body || 'No message returned';
  this.fetchRequests(); 
},
      error: () => this.message = 'Failed to close request'
    });
  }
}
