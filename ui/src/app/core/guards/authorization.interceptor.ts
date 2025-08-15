import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { catchError, finalize, Observable, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LoaderService } from '../../shared/components/loader/loader.service';
import { LocalStorageService } from '@core/local-storage/local-storage.service';
import { Router } from '@angular/router';

@Injectable()
export class AuthorizationInterceptor implements HttpInterceptor {
  constructor(
    private loadingService: LoaderService,
    private localStorageService: LocalStorageService,
    private router: Router
  ) {}

  private totalRequests = 0;

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    this.totalRequests++;
    this.loadingService.setLoading(true);

    const authToken = this.localStorageService.getItem('token');
    if (authToken) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${authToken}`
        }
      });
    }

    return next.handle(request).pipe(
      catchError((error: any) => {
        console.log(error);
        // if (error.status === 0 || error.status === 401 || error.status === 403) {
        //   this.localStorageService.clear();
        //   setTimeout(() => {
        //     this.router.navigate(['/login']);
        //   }, 500);
        // }
        this.loadingService.setLoading(false);
        return throwError(error);
      }),
      finalize(() => {
        this.totalRequests--;
        if (this.totalRequests === 0) {
          this.loadingService.setLoading(false);
        }
      })
    );
  }
}
