import { createSelector } from '@ngrx/store';
import { AppState } from '../app.state';

export const selectOriginalTarget = (state: AppState) => state.originalTarget;

export const selectOriginalTargetEndpoint = createSelector(
    selectOriginalTarget,
    (ot) => ot.target
);