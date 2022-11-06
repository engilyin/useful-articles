
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import {StoreModule} from '@ngrx/store';
import { reducer } from './original-target.reducer';
import { EffectsModule } from '@ngrx/effects';
import { OriginalTargetEffects } from './original-target.effects';
import { originalTargetFeatureKey } from './original-target.const';



@NgModule({
    declarations: [],
    imports: [
      CommonModule,
      StoreModule.forFeature(originalTargetFeatureKey, reducer),
      EffectsModule.forFeature([OriginalTargetEffects])
    ]})
export class OriginalTargetStateModule { }