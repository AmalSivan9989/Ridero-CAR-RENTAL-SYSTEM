import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserData } from '../models/user-data';
import { Car } from '../models/car';
import { Booking } from '../models/booking';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  constructor(private _http: HttpClient) { }

  getUserByUsername(username: string): Observable<UserData> {
    const token = localStorage.getItem('jwt');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this._http.get<UserData>(`http://localhost:2003/customer/getByName/${username}`, { headers });
  }

  getCarsByLocation(token:string,location: string): Observable<Car[]> {
   token = localStorage.getItem('jwt');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    const url = `http://localhost:2003/cars/getCarsByLocation/${location}`;
    return this._http.get<Car[]>(url, { headers }); 
  }

  private baseUrl = 'http://localhost:2003/bookings';
createBooking(booking: any, token: string): Observable<any> {
    const userId = localStorage.getItem('cuid');


    booking.user = { uid: Number(userId) };

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this._http.post<any>(
      `${this.baseUrl}/createBooking`,
      booking,
      { headers, responseType: 'json' }
    );
  }

  getPendingPayments(userId: number, token: string): Observable<Booking[]> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this._http.get<Booking[]>(
      `http://localhost:2003/payments/pendingPayments/${userId}`,
      { headers }
    );
  }


  makePayment(bookingId: number, paymentData: any, token: string): Observable<any> {
  const headers = new HttpHeaders({
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  });
  return this._http.post(
    `http://localhost:2003/payments/makePayment/${bookingId}`,
    paymentData,
    { headers, responseType: 'text' as 'json' }
  );
}

paymentHistory(userId:number, token: string):Observable<any>{
    const headers = new HttpHeaders({
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  });

  return this._http.get<any>(`http://localhost:2003/payments/history/${userId}`,{ headers });
  
}

createWallet(userId: number, token: string): Observable<any> {
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });
  return this._http.post(`http://localhost:2003/wallet/createWallet/${userId}`, {}, { headers });
}

getWalletBalance(userId: number, token: string): Observable<any> {
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });
  return this._http.get(`http://localhost:2003/wallet/getWalletBalance/${userId}`, { headers });
}

addFunds(userId: number, amount: number, token: string): Observable<any> {
  if (!token) {
    throw new Error('JWT token is missing.');
  }

  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });

  return this._http.put(`http://localhost:2003/wallet/addFunds/${userId}/${amount}`, {}, { headers });
}

deductFunds(userId: number, amount: number, token: string): Observable<any> {
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });
  return this._http.put(`http://localhost:2003/wallet/deductFunds/${userId}/${amount}`, {}, { headers });
}

checkInBooking(bookingId: number, token: string): Observable<any> {
  const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` });
  return this._http.put(
    `http://localhost:2003/bookings/checkin/${bookingId}`,
    {}, 
    { headers, responseType: 'text' as 'json' }  
  );
}

getMyBookings(token: string): Observable<Booking[]> {
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });
  return this._http.get<Booking[]>(`http://localhost:2003/bookings/getMyBookings`, { headers });
}

checkOut(bookingId: number, token: string): Observable<any> {
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });
  return this._http.post(`http://localhost:2003/bookings/checkout/${bookingId}`, {}, { headers, responseType: 'text' });
}

submitFeedback(carId: number, feedback: any, token: string): Observable<any> {
  const headers = new HttpHeaders({
    Authorization: `Bearer ${token}`
  });

  return this._http.post(`http://localhost:2003/feedback/submitFeedback?carId=${carId}`, feedback, {
    headers: headers,
    responseType: 'text' as 'json'  
  });
}


}
