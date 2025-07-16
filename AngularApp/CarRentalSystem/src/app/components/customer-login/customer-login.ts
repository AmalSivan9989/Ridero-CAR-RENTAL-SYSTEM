import { Component } from '@angular/core';
import { AuthRequest } from '../../models/auth-request';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../service/auth-service';
import { UserData } from '../../models/user-data';
import { CustomerService } from '../../service/customer-service';

@Component({
  selector: 'app-customer-login',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './customer-login.html',
  styleUrls: ['./customer-login.css']
})
export class CustomerLogin {
  username: string;
  password: string;
  customerFound: UserData;
  jwt: string;

  constructor(
    private authService: AuthService,
    private customerService: CustomerService,
    private router: Router
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

    console.log('Sending auth request:', authRequest);

    this.authService.generateToken(authRequest).subscribe({
      next: token => {
        console.log('Received JWT token:', token);
        this.jwt = token;
        localStorage.setItem('jwt', token);

        
        this.customerService.getUserByUsername(this.username).subscribe({
          next: user => {
            console.log('User data received:', user);
            if (user && user.uid) {
              this.customerFound = user;

              localStorage.setItem('cid', user.username); 
              localStorage.setItem('cuid', user.uid.toString()); 

              console.log('Stored in localStorage - cid:', user.username, ', cuid:', user.uid);

             
              this.router.navigate(['/customerMenu']);
            } else {
              console.warn('User object missing uid:', user);
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
