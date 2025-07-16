import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Payment } from '../../models/payment';
import { AdminService } from '../../service/admin-service';

@Component({
  selector: 'app-show-payments',
  imports: [CommonModule,FormsModule,RouterModule],
  templateUrl: './show-payments.html',
  styleUrl: './show-payments.css'
})
export class ShowPayments {

  payment:Payment[];
  token:string;
   constructor(private _adminService:AdminService){
      this.token=localStorage.getItem("jwt");
      this._adminService.showPayments(this.token).subscribe(x=>{
        this.payment=x;
      })
   }
}
