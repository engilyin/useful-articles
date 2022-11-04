import { Component } from '@angular/core';
import { ThemeService } from './services/sys/theme/theme.service';

@Component({
  selector: 'ua-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'useful-articles-frontend';

  constructor(private themeService: ThemeService) {}

  lightTheme() {
    this.themeService.switchTheme('light');
  }

  darkTheme() {
    this.themeService.switchTheme('dark');
  }
}
