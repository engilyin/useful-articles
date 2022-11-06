import { createSelector } from '@ngrx/store';
import { AppState } from '../app.state';

export const selectGlobalProgressState = (state: AppState) => state.globalProgress;

export const selectIsBusy = createSelector(
    selectGlobalProgressState,
    (progress) => !progress.ready
);