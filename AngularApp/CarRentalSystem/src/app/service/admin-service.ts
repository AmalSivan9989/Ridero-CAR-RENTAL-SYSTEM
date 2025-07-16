import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Car } from '../models/car';
import { UserData } from '../models/user-data';
import { MaintenanceRequest } from '../models/maintenance-request';
import { Feedback } from '../models/feedback';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private _http:HttpClient) { }

  showUsers(token:string): Observable<any>{
var headers_object = new HttpHeaders({
    "Content-Type":"application/json",
    "Authorization":"Bearer "+token
  });
    const httpOptions = {
      headers : headers_object
    }
    return this._http.get("http://localhost:2003/admin/getAllUsers",httpOptions);

  }


  showBookings(token:string):Observable<any>{
var headers_object = new HttpHeaders({
    "Content-Type":"application/json",
    "Authorization":"Bearer "+token
  });
    const httpOptions = {
      headers : headers_object
    }
    return this._http.get("http://localhost:2003/admin/getAllBookings",httpOptions)
  }

  showCars(token:string):Observable<any>{
    var headers_object = new HttpHeaders({
    "Content-Type":"application/json",
    "Authorization":"Bearer "+token
  });
    const httpOptions = {
      headers : headers_object
    }
    return this._http.get("http://localhost:2003/admin/getAllCars",httpOptions);
  }

  addCar(car: Car, token: string): Observable<any> {
  const headers = new HttpHeaders({
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  });
  return this._http.post<string>(
    'http://localhost:2003/cars/addCar',
    car,
    { headers, responseType: 'text' as 'json' }
  );
}

updateAvailability(carId: number, available: boolean, token: string): Observable<any> {
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });

  const url = `http://localhost:2003/cars/updateAvailability/${carId}?available=${available}`;

  return this._http.put(
    url,
    null,  
    { headers, responseType: 'text' as 'json' }
  );
}
showPayments(token:string):Observable<any>{
    var headers_object = new HttpHeaders({
    "Content-Type":"application/json",
    "Authorization":"Bearer "+token
  });
    const httpOptions = {
      headers : headers_object
    }
    return this._http.get("http://localhost:2003/payments/getAllPayments",httpOptions);
  }

deleteCar(carId: number, token: string): Observable<any> {
  const headers = new HttpHeaders({
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  });
  const url = `http://localhost:2003/cars/deleteCar/${carId}`;
  return this._http.delete(url, { headers, responseType: 'text' });
}

addUser(userInfo:UserData):Observable<string>{

     return this._http.post<string>(`http://localhost:2003/auth/addNewUser`,userInfo,{  responseType: 'text' as 'json' })
}
getUserById(id: number): Observable<UserData> {
  const token = localStorage.getItem('jwt');
  if (!token) {
    throw new Error('JWT token not found');
  }

  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });

  return this._http.get<UserData>(
    `http://localhost:2003/getUser/${id}`,
    { headers }
  );
}


  updateUser(id: number, user: UserData, token: string): Observable<UserData> {

   const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });
    return this._http.put<UserData>(`http://localhost:2003/updateUser/${id}`, user, { headers });
  }

  deactivateUser(id: number, token: string): Observable<string> {
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });

  return this._http.put<string>(
    `http://localhost:2003/deactivateUser/${id}`,
    null,
    { headers, responseType: 'text' as 'json' }
  );
}

private baseUrl = 'http://localhost:2003/maintenance';

 

  reportIssue(carId: number, request: MaintenanceRequest, token: string): Observable<string> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    const url = `${this.baseUrl}/reportIssue/${carId}`;
    return this._http.post<string>(url, request, { headers, responseType: 'text' as 'json' });
  }

  getRequestsByAgent(agentId: number, token: string): Observable<MaintenanceRequest[]> {
  const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
  return this._http.get<MaintenanceRequest[]>(`http://localhost:2003/maintenance/getRequestsByAgent/${agentId}`, { headers });
}

  getFullSummary(token: string): Observable<{ [key: string]: any }> {
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });

  return this._http.get<{ [key: string]: any }>(
    'http://localhost:2003/admin/reports/summary',
    { headers }
  );
}

getAllAgents(token: string): Observable<UserData[]> {
  const httpOptions = {
    headers: new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
  };
  return this._http.get<UserData[]>('http://localhost:2003/agent/getAllAgents', httpOptions);
}

getFeedbackByCar(carId: number, token: string): Observable<Feedback[]> {
  const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
  return this._http.get<Feedback[]>(`http://localhost:2003/feedback/getFeedback/${carId}`, { headers });
}

}
