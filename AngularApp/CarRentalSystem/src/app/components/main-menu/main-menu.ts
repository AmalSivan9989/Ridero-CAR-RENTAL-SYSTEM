import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-main-menu',
  imports: [CommonModule,FormsModule,RouterModule],
  templateUrl: './main-menu.html',
  styleUrl: './main-menu.css'
})
export class MainMenu {

}
