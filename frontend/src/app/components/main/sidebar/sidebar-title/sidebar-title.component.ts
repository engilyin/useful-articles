import { Component, OnInit, ElementRef, AfterViewInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '@root/app/store/app.state';
import { selectCurrentRole, selectCurrentUser, selectCurrentUserName } from '@root/app/store/session/session.selectors';
import { Observable } from 'rxjs';

declare var bootstrap:any;

@Component({
  selector: 'ua-sidebar-title',
  templateUrl: './sidebar-title.component.html',
  styleUrls: ['./sidebar-title.component.scss']
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
    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    tooltipTriggerList.forEach(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));
  }

  ngOnInit(): void {
  }

}
