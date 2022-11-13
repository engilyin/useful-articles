import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'ua-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
})
export class MainComponent implements OnInit {
  @ViewChild('sidebar') sidebar?: ElementRef;

  constructor() {}

  ngOnInit(): void {}

  toggleSidebar() {
    console.log(`Sidebar: ${this.sidebar!.nativeElement.className}`)
    if (this.sidebar!.nativeElement.className == 'active') {
      this.sidebar!.nativeElement.className = 'collapse';
    } else {
      this.sidebar!.nativeElement.className = 'active';
    }
  }
}
