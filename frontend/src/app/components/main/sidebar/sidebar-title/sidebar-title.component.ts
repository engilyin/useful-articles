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
import { Component, OnInit, ElementRef, AfterViewInit } from "@angular/core";
import { Store } from "@ngrx/store";
import { AppState } from "@root/app/store/app.state";
import {
  selectCurrentRole,
  selectCurrentUser,
  selectCurrentUserName,
} from "@root/app/store/session/session.selectors";
import { Observable } from "rxjs";

declare var bootstrap: any;

@Component({
  selector: "ua-sidebar-title",
  templateUrl: "./sidebar-title.component.html",
  styleUrls: ["./sidebar-title.component.scss"],
})
export class SidebarTitleComponent implements OnInit, AfterViewInit {
  currentUserId$: Observable<string>;
  currentUserFullName$: Observable<string>;
  currentUserRole$: Observable<string>;

  constructor(private store: Store<AppState>) {
    this.currentUserId$ = this.store.select(selectCurrentUser);
    this.currentUserFullName$ = this.store.select(selectCurrentUserName);
    this.currentUserRole$ = this.store.select(selectCurrentRole);
  }

  ngAfterViewInit(): void {
    const tooltipTriggerList = document.querySelectorAll(
      '[data-bs-toggle="tooltip"]'
    );
    tooltipTriggerList.forEach(
      (tooltipTriggerEl) => new bootstrap.Tooltip(tooltipTriggerEl)
    );
  }

  ngOnInit(): void {}
}
