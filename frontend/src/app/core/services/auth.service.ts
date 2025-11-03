import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable, tap } from 'rxjs';

interface AuthResponse {
  token: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {

  private api = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) {}

  /** 🔹 Реєстрація нового користувача */
  register(name: string, surname: string, email: string, password: string): Observable<any> {
    const body = { name, surname, email, password };
    return this.http.post(`${this.api}/register`, body);
  }

  /** 🔹 Логін і отримання JWT */
  login(email: string, password: string): Observable<AuthResponse> {
    const body = { email, password };
    return this.http.post<AuthResponse>(`${this.api}/login`, body)
      .pipe(
        tap(response => {
          if (response.token) {
            localStorage.setItem('jwt', response.token);
          }
        })
      );
  }

  /** 🔹 Вихід користувача */
  logout(): void {
    localStorage.removeItem('jwt');
  }

  /** 🔹 Перевірка, чи користувач авторизований */
  isLoggedIn(): boolean {
    return !!localStorage.getItem('jwt');
  }

  /** 🔹 Отримати токен */
  getToken(): string | null {
    return localStorage.getItem('jwt');
  }
}
