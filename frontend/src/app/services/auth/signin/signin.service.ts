import { AuthResult } from './../../../models/session/auth-result.model';
import { SigninRequest } from './../../../models/session/signin-request.model';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '@root/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SigninService {

  constructor(private readonly httpClient: HttpClient) {
  }

  signin(signinRequest: SigninRequest): Observable<AuthResult> {
    return this.httpClient.post<AuthResult>(`${environment.baseUrl}/auth/signin`, signinRequest);
  }
}
