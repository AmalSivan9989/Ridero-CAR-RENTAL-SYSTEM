import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-admin-menu',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule, RouterOutlet],
  templateUrl: './admin-menu.html',
  styleUrl: './admin-menu.css'
})
export class AdminMenu {
  constructor(private router: Router) {}

  logout() {
    localStorage.removeItem('jwt');
    localStorage.removeItem('aid');

    this.router.navigate(['/adminLogin']).then(() => {
      window.history.pushState(null, '', location.href);
      window.onpopstate = function () {
        history.go(1);
      };
    });
  }

  goToReport() {
    this.router.navigate(['/adminMenu/fullSummaryReport']);
  }

  openMaintenance() {
    (document.getElementById("maintenanceSidebar") as HTMLElement).style.width = "250px";
  }

  closeMaintenance() {
    (document.getElementById("maintenanceSidebar") as HTMLElement).style.width = "0";
  }
}
