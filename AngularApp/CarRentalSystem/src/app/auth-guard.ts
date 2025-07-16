import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(): boolean {
  const token = localStorage.getItem('jwt');
  console.log('AuthGuard canActivate, token:', token);
  if (token) {
    return true;
  }

  console.log('No token found, redirecting...');
  this.router.navigate(['/']);
  return false;
}
}
