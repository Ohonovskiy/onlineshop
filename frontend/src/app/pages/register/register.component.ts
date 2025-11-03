import { Component } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  name = '';
  surname = '';
  email = '';
  password = '';
  message = '';

  constructor(private authService: AuthService) {}

  register() {
    this.authService.register(this.name, this.surname, this.email, this.password)
      .subscribe({
        next: () => this.message = '✅ Реєстрація успішна!',
        error: err => this.message = '❌ Помилка: ' + err.error
      });
  }
}
