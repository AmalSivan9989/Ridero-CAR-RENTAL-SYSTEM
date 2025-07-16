import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CustomerService } from '../../service/customer-service';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import * as L from 'leaflet';

@Component({
  selector: 'app-create-booking',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './create-booking.html',
  styleUrl: './create-booking.css'
})
export class CreateBooking implements OnInit {
  pickupDate: string = '';
  dropoffDate: string = '';
  pickupLocation: string = '';
  dropoffLocation: string = '';
  carId: number | null = null;
routeLine: any;

  map: any;
  pickupMarker: any;
  dropoffMarker: any;

  constructor(
    private _customerService: CustomerService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const selectedCarId = localStorage.getItem('selectedCarId');
    if (!selectedCarId) {
      alert('No car selected. Please search and select a car first.');
      this.router.navigate(['/customerMenu/showCarsByLocation']);
      return;
    }
    this.carId = +selectedCarId;
    this.initMap();
  }

  initMap(): void {
    this.map = L.map('map').setView([20.5937, 78.9629], 5);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(this.map);
  }

  async updateMap(): Promise<void> {
  if (!this.pickupLocation || !this.dropoffLocation) return;

  const pickupCoords = await this.geocodeLocation(this.pickupLocation);
  const dropoffCoords = await this.geocodeLocation(this.dropoffLocation);

  if (!pickupCoords || !dropoffCoords) return;

 
  if (this.pickupMarker) this.map.removeLayer(this.pickupMarker);
  if (this.dropoffMarker) this.map.removeLayer(this.dropoffMarker);
  this.routeLine?.remove?.();

  this.pickupMarker = L.marker(pickupCoords).addTo(this.map).bindPopup('Pickup Location').openPopup();
  this.dropoffMarker = L.marker(dropoffCoords).addTo(this.map).bindPopup('Dropoff Location').openPopup();

  const group = L.featureGroup([this.pickupMarker, this.dropoffMarker]);
  this.map.fitBounds(group.getBounds(), { padding: [50, 50] });


  const routeRes = await fetch(`https://router.project-osrm.org/route/v1/driving/${pickupCoords.lng},${pickupCoords.lat};${dropoffCoords.lng},${dropoffCoords.lat}?overview=full&geometries=geojson`);
  const routeData = await routeRes.json();

  if (routeData.routes && routeData.routes.length > 0) {
    const routeCoords = routeData.routes[0].geometry.coordinates.map((c: any) => [c[1], c[0]]);
    this.routeLine = L.polyline(routeCoords, { color: 'blue', weight: 4 }).addTo(this.map);
  }
}


  async geocodeLocation(location: string): Promise<{ lat: number; lng: number } | null> {
    try {
      const res = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(location)}`);
      const data = await res.json();
      if (data && data.length > 0) {
        return { lat: parseFloat(data[0].lat), lng: parseFloat(data[0].lon) };
      }
    } catch (err) {
      console.error('Geocoding error:', err);
    }
    return null;
  }

  createBooking(form: NgForm): void {
    if (form.invalid || !this.carId) {
      alert('Please fill all fields correctly.');
      return;
    }

    const token = localStorage.getItem('jwt');
    const userId = localStorage.getItem('cuid');

    if (!token || !userId) {
      alert('You are not logged in. Please login again.');
      this.router.navigate(['/customerLogin']);
      return;
    }

    const bookingData = {
      pickupDate: this.pickupDate,
      dropoffDate: this.dropoffDate,
      checkInTime: this.pickupDate,
      checkOutTime: this.dropoffDate,
      pickupLocation: this.pickupLocation,
      dropoffLocation: this.dropoffLocation,
      car: { id: this.carId }
    };

    this._customerService.createBooking(bookingData, token).subscribe({
      next: (res) => {
        alert('Booking successful! Redirecting to payment...');
        localStorage.setItem('lastBookingId', res.bookingId);
        this.router.navigate(['/customerMenu/pendingPayments']);
      },
      error: (err) => {
        console.error('Booking error:', err);
        alert('Failed to create booking. Please try again.');
      }
    });
  }
}
