import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthRequest } from '../../models/auth-request';
import { AuthService } from '../../service/auth-service';
import { AgentService } from '../../service/agent-service';
import { UserData } from '../../models/user-data';

@Component({
  selector: 'app-agent-login',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './agent-login.html',
  styleUrls: ['./agent-login.css']
})
export class AgentLogin {
  username: string;
  password: string;
  jwt: string;
  customerFound: UserData;
  constructor(
    private authService: AuthService,
    private router: Router,
    private agentService:AgentService
  ) {}

  login(form: NgForm) {
    if (form.invalid) {
      console.log('Form is invalid:', form);
      return;
    }

    const authRequest: AuthRequest = {
      username: this.username,
      password: this.password
    };

    console.log('Sending agent auth request:', authRequest);

    this.authService.generateToken(authRequest).subscribe({
      next: token => {
        console.log('Received JWT token:', token);
        this.jwt = token;
        localStorage.setItem('jwt', token);

       
         this.agentService.getUserByUsername(this.username).subscribe({
          next: user => {
            console.log('User data received:', user);
            if (user && user.uid) {
              this.customerFound = user;

              localStorage.setItem('aid', user.username); 
              localStorage.setItem('auid', user.uid.toString()); 

              console.log('Stored in localStorage - aid:', user.username, ', auid:', user.uid);

             
              this.router.navigate(['/agentMenu']);
            } else {
              console.warn('User object missing aid:', user);
              alert('User not found or invalid data from server.');
            }
          },
          error: err => {
            console.error('Error fetching user details:', err);
            alert('Failed to fetch user details.');
          }
        });
      },
      error: err => {
        console.error('Authentication error:', err);
        alert('Invalid username or password');
      }
    });
  }
  goBack() {
    this.router.navigate(["/mainMenu"]);
  }
}
