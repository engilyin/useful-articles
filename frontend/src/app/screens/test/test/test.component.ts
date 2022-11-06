import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { ThemeService } from '@root/app/services/sys/theme/theme.service';
import { AppState } from '@root/app/store/app.state';
import * as SessionActions from '@store/session/session.actions';

@Component({
  selector: 'ua-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.scss']
})
export class TestComponent implements OnInit {

  constructor(private readonly store: Store<AppState>, private themeService: ThemeService) { }

  ngOnInit(): void {
  }


  lightTheme() {
    this.themeService.switchTheme('light');
  }

  darkTheme() {
    this.themeService.switchTheme('dark');
  }


  doSignoff() {
    this.store.dispatch(SessionActions.signoff());
  }
}
