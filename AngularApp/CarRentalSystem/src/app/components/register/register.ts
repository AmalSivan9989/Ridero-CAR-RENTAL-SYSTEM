import { Component } from '@angular/core';
import { UserData } from '../../models/user-data';
import { AdminService } from '../../service/admin-service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  imports: [CommonModule,FormsModule,RouterModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {
user: UserData = {
    uid: 0,          
    username: '',
    name: '',
    email: '',
    password: '',
    roles: 'ROLE_CUSTOMER',  
    active: true
  };

   message: string = '';
 isLoading = false;
  constructor(private _adminService:AdminService, private router: Router) {}
 register() {
    this.isLoading = true;
    setTimeout(() => {
      this.message = 'Registered successfully!';
      this.isLoading = false;
      console.log('User registered:', this.user);
    }, 2000);
  
  

    this._adminService.addUser(this.user).subscribe({
      next: (res) => {
        this.message = res;
        alert('Registration successful! Please login.');
        this.router.navigate(['/customerLogin']);
      },
      error: (err) => {
        this.message = 'Registration failed. Please try again.';
        console.error(err);
      }
    });
  }

}