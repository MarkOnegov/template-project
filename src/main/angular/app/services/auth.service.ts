import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { LoginDTO } from '../dtos/auth.dto';
import { UserDTO } from '../dtos/user.dto';
import { Logger } from '../utils/logger';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private logger = new Logger(AuthService.name);
  private _isAuthenticated: boolean = false;
  currentUser: UserDTO | null = null;

  constructor(private http: HttpClient) {}

  login(credentials: LoginDTO) {
    this.logger.debug(credentials);
    return this.http.post<UserDTO | null>('/api/auth', credentials).pipe(
      catchError(() => of(null)),
      tap((user) => {
        this._isAuthenticated = !!user;
        this.currentUser = user;
      })
    );
  }

  get isAuthenticated(): Observable<boolean> {
    if (this._isAuthenticated) {
      return of(true);
    }
    return this.http.get<UserDTO | null>('/api/auth').pipe(
      tap((user) => {
        this._isAuthenticated = !!user;
        this.currentUser = user;
      }),
      map((user) => !!user)
    );
  }
}
