import { ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TestComponent } from './test/test.component';
import { TestRoutingModule } from './test-routing.module';
import { WidgetsModule } from '@root/app/components/widgets/widgets.module';



@NgModule({
  declarations: [TestComponent],
  imports: [
    CommonModule,
    TestRoutingModule,
    ReactiveFormsModule,
    WidgetsModule
  ]
})
export class TestModule { }
