import { Injectable } from '@angular/core';
import { Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthenticationService } from '@core/auth/authentication.service';
import { environment } from '@env/environment';
import { Observable } from 'rxjs';

@Injectable()
export class AuthGuardService  {

    constructor(private router: Router, private authenticationService: AuthenticationService) { }

    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<boolean> | Promise<boolean> | boolean {

      this.authenticationService.isLoggedIn$.subscribe((val: boolean) => {
        if (!val) {
          this.router.navigate(['/login']);
        }
        return true;
      });
      return true;
    }

}
