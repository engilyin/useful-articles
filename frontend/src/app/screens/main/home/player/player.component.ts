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
import { Component, ViewChild, ElementRef, AfterViewInit } from "@angular/core";
import { VgApiService } from "@videogular/ngx-videogular/core";
import { Subject } from "rxjs";

@Component({
  selector: "ua-player",
  templateUrl: "./player.component.html",
  styleUrls: ["./player.component.scss"],
})
export class PlayerComponent implements AfterViewInit {
  @ViewChild("player") player!: ElementRef;

  api!: VgApiService;

  streamUrl$: Subject<string> = new Subject<string>();

  constructor() {}

  ngAfterViewInit(): void {
    this.player.nativeElement.addEventListener(
      "show.bs.modal",
      (event: any) => {
        const item: HTMLElement = event.relatedTarget;

        this.streamUrl$.next(item.getAttribute("data-bs-stream") as string);
      }
    );

    this.player.nativeElement.addEventListener(
      "hide.bs.modal",
      (event: any) => {
        this.api.pause();
      }
    );
  }

  onPlayerReady(api: VgApiService) {
    this.api = api;
  }
}
