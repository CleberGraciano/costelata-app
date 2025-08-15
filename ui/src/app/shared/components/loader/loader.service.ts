import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LoaderService {
  loading$ = new BehaviorSubject<boolean>(false);
  isLoading$ = this.loading$.asObservable();

  constructor() {}

  setLoading(loading: boolean) {
    this.loading$.next(loading);
  }
}
