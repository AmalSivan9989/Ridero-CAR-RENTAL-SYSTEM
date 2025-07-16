import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Car } from '../../models/car';
import { AdminService } from '../../service/admin-service';
import { CarStatus } from '../../models/car-status';

@Component({
  selector: 'app-add-car',
  imports: [CommonModule,FormsModule,RouterModule],
  templateUrl: './add-car.html',
  styleUrl: './add-car.css'
})
export class AddCar {
  car:Car;
   carStatuses = Object.values(CarStatus);
  isValid:boolean;
  result:string;

  addCar(carForm:NgForm){
    if(carForm.invalid){
        return;
    }
    this.isValid=true;
    const token = localStorage.getItem('jwt');
    this._adminService.addCar(this.car,token).subscribe(x=>{
      this.result=x;
    })

  }

 constructor(private _adminService:AdminService) {
    this.isValid = false;
    this.car=new Car();
  }

  
}
