import { Component, OnInit } from '@angular/core';
import { ThemeService } from '@root/app/services/sys/theme/theme.service';

@Component({
  selector: 'ua-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.scss']
})
export class TestComponent implements OnInit {

  constructor(private themeService: ThemeService) { }

  ngOnInit(): void {
  }


  lightTheme() {
    this.themeService.switchTheme('light');
  }

  darkTheme() {
    this.themeService.switchTheme('dark');
  }
}
