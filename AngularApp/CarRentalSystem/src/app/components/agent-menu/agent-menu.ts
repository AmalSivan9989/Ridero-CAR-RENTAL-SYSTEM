import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-agent-menu',
  imports: [CommonModule,FormsModule,RouterModule],
  templateUrl: './agent-menu.html',
  styleUrl: './agent-menu.css'
})
export class AgentMenu {
constructor(private router: Router) {}

  logout() {
    localStorage.removeItem('jwt');
    localStorage.removeItem('aid');

    this.router.navigate(['/agentLogin']).then(() => {
      window.history.pushState(null, '', location.href);
      window.onpopstate = function () {
        history.go(1);
      };
    });
  }
}
