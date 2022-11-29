import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';

@Component({
  selector: 'ua-player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements AfterViewInit {

  @ViewChild('player') player!: ElementRef;

  streamUrl?: string;

  constructor() { }

  ngAfterViewInit(): void {

    this.player.nativeElement.addEventListener('show.bs.modal', (event: any) => {

      const item: HTMLElement = event.relatedTarget;

      this.streamUrl = item.getAttribute('data-bs-stream') as string;

    })

  }

}
