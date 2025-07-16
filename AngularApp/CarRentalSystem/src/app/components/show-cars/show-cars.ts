import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Car } from '../../models/car';
import { AdminService } from '../../service/admin-service';

@Component({
  selector: 'app-show-cars',
  imports: [CommonModule,FormsModule,RouterModule],
  templateUrl: './show-cars.html',
  styleUrl: './show-cars.css'
})
export class ShowCars {
    cars:Car[];
    token:string;
    constructor(private _adminService:AdminService) {
       this.token = localStorage.getItem("jwt");
       this._adminService.showCars(this.token).subscribe(x=>{
        this.cars=x;
       })

}}
