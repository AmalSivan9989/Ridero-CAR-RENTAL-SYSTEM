import { Car } from "./car";
import { UserData } from "./user-data";

export class Feedback {
    id: number;
  comment: string;
  rating: number;
  user: UserData;
  car: Car;
}
