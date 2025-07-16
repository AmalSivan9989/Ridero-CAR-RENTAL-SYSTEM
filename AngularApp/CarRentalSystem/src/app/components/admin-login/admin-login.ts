import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthRequest } from '../../models/auth-request';
import { AuthService } from '../../service/auth-service';

@Component({
  selector: 'app-admin-login',
  imports: [CommonModule,FormsModule,RouterModule],
  templateUrl: './admin-login.html',
  styleUrl: './admin-login.css'
})
export class AdminLogin {

  userName:string;
  passWord:string;
  jwt:string;
  authRequest:AuthRequest;


  generate(){
    this.authRequest.username=this.userName;
    this.authRequest.password=this.passWord;
    this._authService.generateToken(this.authRequest).subscribe(x=>{
      localStorage.setItem("jwt",x);
      this.jwt = x;
      this._router.navigate(["/adminMenu"])
    })
  }

constructor(private _authService : AuthService,private _router : Router) {
    this.authRequest = new AuthRequest();
  }
  goBack() {
    this._router.navigate(["/mainMenu"]);
  }
}
