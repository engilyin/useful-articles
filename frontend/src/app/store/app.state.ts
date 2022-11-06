import { GlobalProgressState } from './global-progress/global-progress.state';
import { OriginalTargetState } from './original-target/original-target.state';
import { SessionState } from "./session/session.state";

export interface AppState {
    session: SessionState;
    globalProgress: GlobalProgressState;
    originalTarget: OriginalTargetState;
}
