import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Booking } from '../../models/booking';
import { UserData } from '../../models/user-data';
import { Car } from '../../models/car';
import { AdminService } from '../../service/admin-service';

@Component({
  selector: 'app-show-bookings',
  imports: [CommonModule,FormsModule,RouterModule],
  templateUrl: './show-bookings.html',
  styleUrl: './show-bookings.css'
})
export class ShowBookings {

  bookings:Booking[];
  token:string;
  expandedIndex: number | null = null;
  constructor(private _adminService:AdminService) {
     this.token = localStorage.getItem("jwt");
     this._adminService.showBookings(this.token).subscribe(x=>{
      this.bookings=x;
     })
  }
  toggleDetails(index: number) {
    this.expandedIndex = this.expandedIndex === index ? null : index;
  }


}
