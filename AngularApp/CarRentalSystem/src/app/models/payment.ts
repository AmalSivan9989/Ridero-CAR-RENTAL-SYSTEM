import { Booking } from "./booking";

export class Payment {
  id: number;
  amount: number;
  paymentMethod: string; 
  paymentTime: string;   
  booking: Booking;
  status: string; 
}