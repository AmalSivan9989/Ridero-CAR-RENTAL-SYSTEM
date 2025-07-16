import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MaintenanceRequest } from '../models/maintenance-request';
import { UserData } from '../models/user-data';

@Injectable({
  providedIn: 'root'
})
export class AgentService {

  constructor(private _http:HttpClient) { }

  closeMaintenanceRequest(requestId: number, agentId: number, token: string): Observable<HttpResponse<string>> {
  const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
  const url = `http://localhost:2003/maintenance/closeRequest/${requestId}/agent/${agentId}`;
  return this._http.put(url, null, { headers, observe: 'response', responseType: 'text' });
}

getRequestsByAgent(agentId: number, token: string): Observable<MaintenanceRequest[]> {
  const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
  return this._http.get<MaintenanceRequest[]>(`http://localhost:2003/maintenance/getRequestsByAgent/${agentId}`, { headers });
}

getUserByUsername(username: string): Observable<UserData> {
    const token = localStorage.getItem('jwt');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this._http.get<UserData>(`http://localhost:2003/agent/getByName/${username}`, { headers });
  }
 

 private baseUrl = 'http://localhost:2003/maintenance';
  getRequestsIncludingUnassigned(agentId: number, token: string): Observable<MaintenanceRequest[]> {
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    return this._http.get<MaintenanceRequest[]>(`${this.baseUrl}/requests?agentId=${agentId}`, { headers });
  }


  assignRequestToAgent(requestId: number, agentId: number, token: string): Observable<HttpResponse<any>> {
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    
    return this._http.post<any>(
      `${this.baseUrl}/assign?requestId=${requestId}&agentId=${agentId}`,
      null,
      { headers, observe: 'response' }
    );
  }

 

  
}
