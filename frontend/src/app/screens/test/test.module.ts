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
import { ReactiveFormsModule } from "@angular/forms";
import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { TestComponent } from "./test/test.component";
import { TestRoutingModule } from "./test-routing.module";
import { WidgetsModule } from "@root/app/components/widgets/widgets.module";

@NgModule({
  declarations: [TestComponent],
  imports: [
    CommonModule,
    TestRoutingModule,
    ReactiveFormsModule,
    WidgetsModule,
  ],
})
export class TestModule {}
