import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatSnackBarModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule
  ]
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  loginForm = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  private baseUrl = 'http://localhost:8084/api/auth'; // Your auth-service URL

  login() {
    if (this.loginForm.invalid) return;

    const { username, password } = this.loginForm.value;

    this.http.post<any>(`${this.baseUrl}/login`, { username, password }).subscribe({
      next: (res) => {
        // Save JWT, username, role
        localStorage.setItem('token', res.token);
        localStorage.setItem('username', res.username);
        localStorage.setItem('role', res.role);

        this.snackBar.open('Login successful', 'Close', { duration: 2000 });
        this.router.navigate(['/appointments']);
      },
      error: (err) => {
        console.error('Login error:', err);
        this.snackBar.open('Invalid username or password', 'Close', { duration: 3000 });
      }
    });
  }
}
