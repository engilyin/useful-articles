import { ActionReducer } from '@ngrx/store';
import { localStorageSync } from 'ngrx-store-localstorage';
import { sessionFeatureKey } from '@store/session/session.const';

export function localStorageSyncReducer(reducer: ActionReducer<any>): ActionReducer<any> {
  return localStorageSync({
    keys: [sessionFeatureKey],
    rehydrate: true,
    removeOnUndefined: true
  })(reducer);
}