import { Component, OnInit } from '@angular/core';
import { AuthService } from './services/auth.service';
import { Logger } from './utils/logger';

@Component({
  selector: 'app-root',
  template: '<router-outlet></router-outlet>',
})
export class AppComponent implements OnInit {
  private logger = new Logger(AppComponent.name);

  ngOnInit(): void {
    this.authService.isAuthenticated.subscribe((isAuthenticated) => {
      if (isAuthenticated) {
        this.logger.debug('CurrentUser', this.authService.currentUser);
      }
    });
  }

  constructor(private authService: AuthService) {}
}
