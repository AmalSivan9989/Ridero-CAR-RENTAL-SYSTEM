import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { UserData } from '../../models/user-data';
import { AdminService } from '../../service/admin-service';

@Component({
  selector: 'app-show-users',
  imports: [CommonModule,FormsModule,RouterModule],
  templateUrl: './show-users.html',
  styleUrl: './show-users.css'
})
export class ShowUsers {
users:UserData[];
token:string;
showPassword: boolean[] = [];
constructor(private _adminService:AdminService) {
    this.token = localStorage.getItem("jwt");
    this._adminService.showUsers(this.token).subscribe(x=>{
      this.users=x;
    })

}

 maskedPassword(password: string): string {
    return '•'.repeat(password.length);
  }

  togglePassword(index: number): void {
    this.showPassword[index] = !this.showPassword[index];
  }
}

