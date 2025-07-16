import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthRequest } from '../models/auth-request';
import { Observable, tap } from 'rxjs';
import { UserData } from '../models/user-data';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private _http:HttpClient) { }

  generateToken(authRequest:AuthRequest):Observable<any>{
    const headers = { 'Content-Type': 'application/json' };
    return this._http.post("http://localhost:2003/auth/generateToken",authRequest,
    {responseType:'text'}).pipe(
      tap((resp) => {
        alert(resp);
      })
    )
  }
  
}
