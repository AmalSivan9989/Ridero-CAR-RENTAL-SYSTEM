import { Car } from "./car";
import { UserData } from "./user-data";

export class Booking {
  id: number;
  pickupDate: string;
  dropoffDate: string;
  pickupLocation: string;
  dropoffLocation: string;
  car: Car;
  user: UserData;
  checkInTime: string;
  checkOutTime: string;
  status: string;
  totalAmount: number;
}
