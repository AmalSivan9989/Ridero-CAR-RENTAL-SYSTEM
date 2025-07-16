import { CarStatus } from "./car-status";

export class Car {
  id: number;
  make: string;
  model: string;
  location: string;
  type: string; 
  imageUrl: string;
  pricePerDay: number;
  available: boolean = true;
  status: CarStatus;
}