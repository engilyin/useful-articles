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
import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";

@Component({
  selector: "ua-main",
  templateUrl: "./main.component.html",
  styleUrls: ["./main.component.scss"],
})
export class MainComponent implements OnInit {
  @ViewChild("sidebar") sidebar?: ElementRef;

  constructor() {}

  ngOnInit(): void {}

  toggleSidebar() {
    console.log(`Sidebar: ${this.sidebar!.nativeElement.className}`);
    if (this.sidebar!.nativeElement.className == "active") {
      this.sidebar!.nativeElement.className = "collapse";
    } else {
      this.sidebar!.nativeElement.className = "active";
    }
  }
}
