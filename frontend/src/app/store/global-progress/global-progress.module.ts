
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import {StoreModule} from '@ngrx/store';
import { reducer } from './global-progress.reducer';
import { globalProgressFeatureKey } from './global-progress.const';



@NgModule({
    declarations: [],
    imports: [
      CommonModule,
      StoreModule.forFeature(globalProgressFeatureKey, reducer)
    ]})
export class GlobalProgressStateModule { }


