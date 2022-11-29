import { Component, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import {VgApiService} from '@videogular/ngx-videogular/core';
import { Subject } from 'rxjs';

@Component({
  selector: 'ua-player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements AfterViewInit {

  @ViewChild('player') player!: ElementRef;

  api!: VgApiService;

  streamUrl$: Subject<string> = new Subject<string>();

  constructor() { }

  ngAfterViewInit(): void {

    this.player.nativeElement.addEventListener('show.bs.modal', (event: any) => {

      const item: HTMLElement = event.relatedTarget;

      this.streamUrl$.next(item.getAttribute('data-bs-stream') as string);

    });


    this.player.nativeElement.addEventListener('hide.bs.modal', (event: any) => {

      this.api.pause();

    });

  }

  onPlayerReady(api: VgApiService) {
    this.api = api;
  }

}
