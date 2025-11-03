import { Component, OnInit } from '@angular/core';
import { TestService } from './core/services/test.service';

@Component({
  selector: 'app-root',
  standalone: true,                         // 🟢 обов'язково для standalone
  imports: [],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  message = 'Loading...';

  constructor(private testService: TestService) {}

  ngOnInit(): void {
    this.testService.ping().subscribe({
      next: (res) => this.message = res,
      error: (err) => this.message = '❌ ' + err.message
    });
  }
}
