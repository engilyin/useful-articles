
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import {StoreModule} from '@ngrx/store';
import { reducer } from './session.reducer';
import { EffectsModule } from '@ngrx/effects';
import { SessionEffects } from './session.effects';
import { sessionFeatureKey } from './session.const';



@NgModule({
    declarations: [],
    imports: [
      CommonModule,
      StoreModule.forFeature(sessionFeatureKey, reducer),
      EffectsModule.forFeature([SessionEffects])
    ]})
export class SessionStateModule { }