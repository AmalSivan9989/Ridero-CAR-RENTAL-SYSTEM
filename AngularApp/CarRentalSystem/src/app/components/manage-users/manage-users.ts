import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AdminService } from '../../service/admin-service';
import { UserData } from '../../models/user-data';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-manage-users',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    MatSnackBarModule,
    MatTableModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule
  ],
  templateUrl: './manage-users.html',
  styleUrls: ['./manage-users.css']
})
export class ManageUsers implements OnInit {
  users: UserData[] = [];
  displayedColumns: string[] = ['uid', 'username', 'email', 'roles', 'active', 'actions'];
  userForm!: FormGroup;
  isEditMode = false;

  roleOptions: string[] = ['admin', 'agent', 'customer'];

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.userForm = this.fb.group({
      uid: [0],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      roles: ['customer', Validators.required]
    });

    this.loadUsers();
  }

  get token(): string {
    return localStorage.getItem('jwt') || '';
  }

  loadUsers(): void {
    this.adminService.showUsers(this.token).subscribe({
      next: (data: UserData[]) => {
        this.users = data.map((user: UserData) => ({
          ...user,
          username: user.username ?? user.name 
        }));
      },
      error: () => this.snackBar.open('Failed to load users', 'Close', { duration: 3000 })
    });
  }

  submitUser(): void {
    if (this.userForm.invalid) return;

    const formValue = this.userForm.value;

    const user: UserData = {
      uid: formValue.uid ?? 0,
      username: formValue.username ?? '',
      name: formValue.username ?? '',
      email: formValue.email ?? '',
      password: formValue.password ?? '',
      roles: formValue.roles ?? 'customer',
      active: true
    };

    if (this.isEditMode) {
      this.adminService.updateUser(user.uid!, user, this.token).subscribe({
        next: () => {
          this.snackBar.open('User updated', 'Close', { duration: 2000 });
          this.loadUsers();
          this.resetForm();
        },
        error: () => this.snackBar.open('Update failed', 'Close', { duration: 3000 })
      });
    } else {
      this.adminService.addUser(user).subscribe({
        next: () => {
          this.snackBar.open('User added', 'Close', { duration: 2000 });
          this.loadUsers();
          this.resetForm();
        },
        error: () => this.snackBar.open('Add failed', 'Close', { duration: 3000 })
      });
    }
  }

  editUser(user: UserData): void {
    this.userForm.patchValue({
      uid: user.uid,
      username: user.username ?? user.name,
      email: user.email,
      password: user.password,
      roles: user.roles
    });
    this.isEditMode = true;
  }

  deleteUser(id: number): void {
    if (!confirm('Are you sure to deactivate this user?')) return;

    this.adminService.deactivateUser(id, this.token).subscribe({
      next: () => {
        this.snackBar.open('User deactivated', 'Close', { duration: 2000 });
        this.loadUsers();
      },
      error: () => this.snackBar.open('Deactivation failed', 'Close', { duration: 3000 })
      });
  }

  resetForm(): void {
    this.userForm.reset({
      uid: 0,
      username: '',
      email: '',
      password: '',
      roles: 'customer'
    });
    this.isEditMode = false;
  }
}
