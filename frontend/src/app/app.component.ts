import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { ThemeService } from './services/sys/theme/theme.service';
import { AppState } from './store/app.state';
import { selectIsBusy } from './store/global-progress/global-progress.selectors';

@Component({
  selector: 'ua-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'useful-articles-frontend';

  globallyBusy: Observable<boolean>;

  constructor(private store: Store<AppState>) {
    this.globallyBusy = this.store.select(selectIsBusy);
  }
}
