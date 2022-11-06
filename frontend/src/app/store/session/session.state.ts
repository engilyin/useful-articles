

export interface SessionState {
  readonly username: string;
  readonly name: string;
  readonly role: string;
  readonly token: string;
  readonly lastError: string;
}

export const initialSessionState: SessionState = {
  username: '',
  name: '',
  role: '',
  token: '',
  lastError: ''
};