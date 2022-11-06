import { OriginalTargetStateModule } from './original-target/original-target.module';
import { GlobalProgressStateModule } from './global-progress/global-progress.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MetaReducer, StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { SessionStateModule } from './session/session-state.module';
import { environment } from '@root/environments/environment';
import { localStorageSyncReducer } from '@store/local-storage.meta-reducer';
import { DefaultDataServiceConfig } from '@ngrx/data';

const metaReducers: Array<MetaReducer<any, any>> = [localStorageSyncReducer];

const defaultDataServiceConfig: DefaultDataServiceConfig = {
  root: `${environment.baseUrl}/api`,
}

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    StoreModule.forRoot({}, {
      metaReducers,
      runtimeChecks: {
        //default value is true
        strictStateImmutability: true,
        strictActionImmutability: true,
        //default value is false
        strictStateSerializability: true,
        strictActionSerializability: false,
        strictActionWithinNgZone: true,
        strictActionTypeUniqueness: true
      }
    }),
    !environment.production ? StoreDevtoolsModule.instrument() : [],
    EffectsModule.forRoot([]),
    SessionStateModule,
    GlobalProgressStateModule,
    OriginalTargetStateModule
  ],
  providers: [{ provide: DefaultDataServiceConfig, useValue: defaultDataServiceConfig }]
})
export class RootStoreModule {
}
