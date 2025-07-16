import { Car } from "./car";
import { UserData } from "./user-data";

export interface MaintenanceRequest {
  id?: number;
  issueDescription: string;
  requestDate?: string; 
  status?: string;
  car?: Car;
  agent?: UserData;
}