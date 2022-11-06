export interface GlobalProgressState {
    readonly ready: boolean;
  }
  
  export const initialGlobalProgressState: GlobalProgressState = {
    ready: true,
  };