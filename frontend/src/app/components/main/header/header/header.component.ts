/*
 Copyright 2022-2025 engilyin

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
import { Component, EventEmitter, OnInit, Output } from "@angular/core";
import { Store } from "@ngrx/store";
import { ThemeService } from "@root/app/services/sys/theme/theme.service";
import { AppState } from "@root/app/store/app.state";
import * as SessionActions from "@store/session/session.actions";

@Component({
  selector: "ua-header",
  templateUrl: "./header.component.html",
  styleUrls: ["./header.component.scss"],
})
export class HeaderComponent implements OnInit {
  @Output() toggleSidebar: EventEmitter<any> = new EventEmitter();

  constructor(
    private readonly store: Store<AppState>,
    private themeService: ThemeService
  ) {}

  ngOnInit(): void {}

  lightTheme() {
    this.themeService.switchTheme("light");
  }

  darkTheme() {
    this.themeService.switchTheme("dark");
  }

  doSignoff() {
    this.store.dispatch(SessionActions.signoff());
  }

  onToggleSidebar() {
    this.toggleSidebar.emit();
  }
}
